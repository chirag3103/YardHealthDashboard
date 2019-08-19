package com.navis.dashboard;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 Data structure for yard information.
 */
public class Yard implements YardInterface {

    // Instantiates a yard with data from a DataStoreConnection.
    Yard() {

                        //MongoDB Part
        // Mongo Connection Driver
        DataStoreConnection connection = new MongoConnection();

        //Collection Operations
        addFromUnitDescriptions(connection.selectUnitDescriptions());
        addFromWorkInstructions(connection.selectWorkInstructions());

                        //MySQL Part
        //MySQL Connection Driver
        //DataStoreConnection connection1 = new MySQLConnection();

        //Collection Operations
        //addFromUnitDescriptions(connection1.selectUnitDescriptions());
        //addFromWorkInstructions(connection1.selectWorkInstructions());

    }

    // Hashmap of all the blocks in the yard.
    public static HashMap<String, Table<String, String, ArrayList<Container>>> yard = new HashMap<>();

    @Override
    public ArrayList<String> getBlocks() {
        //BusinessLogic businessLogic=new BusinessLogic();
        //parse through all the keys in the hash map for the blocks
        for (String blockid: yard.keySet()){
            System.out.println("Unique Block "+ blockid);
            Table<String, String, ArrayList<Container>> block = yard.get(blockid);
            //ArrayList<Container> stack = entry.get(row, col);
            //yard.get(blockid).
            for (ArrayList<Container> stack:block.values()) {
                System.out.print(stack.get(0).getUnitPosition().substring(10,13));
                System.out.print(": This stack has #" +stack.size() + " Container/s");
                System.out.println("and the rehandles in this stack are ");
                //System.out.println(stack.get(0).getUnitPosition().substring(10,13));
                    for(Container container:stack){
                        //System.out.println(block.columnKeySet());
                        System.out.println(container.getUnitNbr()+" container is at location "+container.getUnitPosition());
                    }
            }
        }



        /*
        String blockID = (String) unitPosition.subSequence(6, 10);
        String col = (String) unitPosition.subSequence(11, 13);
        String row = (String) unitPosition.subSequence(10, 11);
        Container container = new Container(unitNbr, unitPosition, estMvTime, kind);
        int tier = unitPosition.charAt(unitPosition.length() - 1) - 65;
        if ( !yard.containsKey(blockID )) {
            //System.out.println(blockID);
            yard.put(blockID, HashBasedTable.create());
        }
        Table<String, String, ArrayList<Container>> block = yard.get(blockID);
        if (!block.contains(row, col))
            block.put(row, col, new ArrayList<>());

        ArrayList<Container> stack = block.get(row, col);
        // Overwrite a container at the same tier.
        for (Container c: stack) {
            if (c.getTier() == tier) {
                stack.set(stack.indexOf(c), container);
                return;
            }
        }

        yard.get(blockID).get(row, col).add(container);

        */


        return null;
    }

    @Override
    public ArrayList<ArrayList<Container>> getStacks() {
            ArrayList<ArrayList<Container>> result = new ArrayList<>();
            for (Table block : yard.values()) {
                result.addAll(block.values());
            }
            return result;
    }

    @Override
    public  ArrayList<ArrayList<Container>> getStacks(String block) {
        ArrayList<ArrayList<Container>> result = new ArrayList<>();
        Table<String, String, ArrayList<Container>> blockconts = yard.get(block);

        result.addAll(blockconts.values());

        return result;
    }

    @Override
    public  ArrayList<ArrayList<Container>> getRow(String row) {
        return new  ArrayList<ArrayList<Container>>();
    }

    @Override
    public  ArrayList<ArrayList<Container>> getColumn(String col) {
        return new  ArrayList<ArrayList<Container>>();
    }

    @Override
    public void addContainer(String unitNbr, String unitPosition, long estMvTime, String kind) {
        String blockID = (String) unitPosition.subSequence(6, 10);
        String col = (String) unitPosition.subSequence(11, 13);
        String row = (String) unitPosition.subSequence(10, 11);
        Container container = new Container(unitNbr, unitPosition, estMvTime, kind);
        int tier = unitPosition.charAt(unitPosition.length() - 1) - 65;
        if ( !yard.containsKey(blockID )) {
            //System.out.println(blockID);
            yard.put(blockID, HashBasedTable.create());
        }
        Table<String, String, ArrayList<Container>> block = yard.get(blockID);
        if (!block.contains(row, col))
            block.put(row, col, new ArrayList<>());

        ArrayList<Container> stack = block.get(row, col);
        // Overwrite a container at the same tier.
        for (Container c: stack) {
            if (c.getTier() == tier) {
                stack.set(stack.indexOf(c), container);
                return;
            }
        }

        yard.get(blockID).get(row, col).add(container);
    }

    // Returns an epoch representing DATETIME.
    public static long getContainerTimeEpoch(String dateTime) {
        if (dateTime == null) {
            return  Long.MAX_VALUE;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
        Date date = null;
        try {
            date = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch = date.getTime();
        return epoch;
    }

    // Adds containers to yard from a WorkInstructions document FINDITERABLE.
    public void addFromWorkInstructions(List<Document> findIterable) {
        String unitNbr;
        String unitPosition;
        String estMvTime;
        String kind;

        for (Document doc : findIterable) {

            final int stdPosStrLen = 15;
            // Check for incomplete entries and record only complete entries.
            if (!doc.getString("Unit Nbr").isEmpty() && !doc.getString("Unit Position").isEmpty()
                    && doc.getString("Unit Position").length() == stdPosStrLen) {
                unitNbr = doc.getString("Unit Nbr");
                unitPosition = doc.getString("Unit Position");
                kind = doc.getString("Kind");

                //System.out.println(unitNbr+unitPosition+kind);
                if (doc.getString("Est Mv Time").isEmpty()) {
                    estMvTime = null;
                } else {
                    estMvTime = doc.getString("Est Mv Time");
                }
                addContainer(unitNbr, unitPosition, getContainerTimeEpoch(estMvTime), kind);
            }
        }

    }

    // Adds containers to yard from a UnitDescriptions document FINDITERABLE.
    public void addFromUnitDescriptions(List<Document> findIterable) {
        String unitNbr;
        String unitPosition;
        for (Document doc : findIterable) {
            final int stdPosStrLen = 15;
            //check for incomplete entries and record only complete entries
            if (!doc.getString("Unit Nbr").isEmpty() && !doc.getString("Position").isEmpty() &&
                    doc.getString("Position").length() == stdPosStrLen) {
                unitNbr = doc.getString("Unit Nbr");
                unitPosition = doc.getString("Position");
                //start processing all the information about the container
                addContainer(unitNbr, unitPosition, Long.MAX_VALUE, " ");
            }
        }
    }

}
