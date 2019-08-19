package com.navis.dashboard;

import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BusinessLogic {

    // Yard object with container information about the yard.
    //Yard yard;

    //HashMap<String, Integer> stackRehandles

    static Yard yard = new Yard();


    // Find the health of the yard.
    public static void main(String[] args) throws IOException {
        System.out.println(getRehandlesInYard(yard) + " total rehandles in yard.");
        //System.out.println(getJson());
        getJson();
        //displays the set of blocks in the yard
        //yard.getBlocks();

    }
    public static String getJson() throws IOException {

        int blockContainers;
        int yardContainers=0;
        int bestBlock=100;

        String bestblock="";
        int worstBlock=0;
        String worstblock="";
        int temp=0;
        JsonObject yard_object= new JsonObject();

        //contains all the block objects
        JsonArray block_array = new JsonArray();

        for (String blockid: yard.yard.keySet()) {
            blockContainers=0;
            //create a block object
            JsonObject block_object = new JsonObject();
            //add the block id to the block object
            block_object.addProperty("block_id",blockid);

            Table<String, String, ArrayList<Container>> block = yard.yard.get(blockid);

            //stack array consists of all the rows and columns
            JsonArray stack_array = new JsonArray();

            for (ArrayList<Container> stack:block.values()) {

                JsonObject stack_object = new JsonObject();

                //assign the id to a property
                stack_object.addProperty("stack_id",stack.get(0).getUnitPosition().substring(10,13));

                stack_object.addProperty("col_id",stack.get(0).getUnitPosition().charAt(10));

                stack_object.addProperty("row_id",stack.get(0).getUnitPosition().substring(11,13));

                stack_object.addProperty("Size",stack.size());

                //String stack_array = new Gson().toJson(stack);

                JsonArray container_array=new JsonArray();

                for(Container container:stack){

                    JsonObject container_object=new JsonObject();

                    container_object.addProperty("Unit Nbr",container.getUnitNbr());
                    container_object.addProperty("Unit Position",container.getUnitPosition());
                    container_object.addProperty("Est Mv Time",container.getEstMvTime());
                    container_object.addProperty("Unit Tier",container.getTier());
                    container_object.addProperty("Kind",container.getKind());

                    container_array.add(container_object);
                    blockContainers++;
                }

                //assign the container array to a property as a string
                stack_object.add("containers",container_array);

                //assign the stack rehandles
                stack_object.addProperty("stack_rehandles",getRehandlesInStack(stack));
                //assign the column array
                stack_array.add(stack_object);
            }
            block_object.add("stack",stack_array);
            block_object.addProperty("block_rehandles",getRehandlesInBlock(blockid));
            //String json = new Gson().toJson(block.values());
            if(getRehandlesInBlock(blockid)<bestBlock){
                bestBlock=getRehandlesInBlock(blockid);
                bestblock=blockid;
            }
            if(getRehandlesInBlock(blockid)>worstBlock){
                worstBlock=getRehandlesInBlock(blockid);
                worstblock=blockid;
            }
            temp=temp+getRehandlesInBlock(blockid);
            block_array.add(block_object);
            //yard_object.add("block",block_object);
            //System.out.println(gson.toJson(block_object));
            System.out.println("Total containers inside "+blockid+" block are: "+blockContainers);
            yardContainers+=blockContainers;
            block_object.addProperty("total_block_containers",blockContainers);
        }

        yard_object.add("blocks",block_array);
        yard_object.addProperty("yard_rehandles",getRehandlesInYard(yard));


        JsonObject bestBlockObject= new JsonObject();
        bestBlockObject.addProperty("best_block",bestblock);
        bestBlockObject.addProperty("block_rehandles",bestBlock);
        yard_object.add("best_block",bestBlockObject);

        JsonObject worstBlockObject= new JsonObject();
        worstBlockObject.addProperty("worst_block",worstblock);
        worstBlockObject.addProperty("block_rehandles",worstBlock);
        yard_object.add("worst_block",worstBlockObject);

        yard_object.addProperty("total_yard_containers",yardContainers);

        System.out.println("Total containers inside all blocks are: "+yardContainers);
        Gson obj = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = obj.toJson(yard_object);

        System.out.println(prettyJson);
        //System.out.println("Best block is:"+bestblock+" with "+bestBlock+" rehandles.");
        //System.out.println("Worst block is:"+worstblock+" with "+worstBlock+" rehandles.");

        //for writing stuff in a json file
        //FileWriter fw=new FileWriter("C:\\Users\\agawach\\Documents\\data.json");
        //fw.write(prettyJson);
        //fw.close();

        return prettyJson;
    }



    // Returns the total number of rehandles in the YARD.
    public static int getRehandlesInYard(Yard yard) {
        int rehandles = 0;
        for (ArrayList<Container> stack : yard.getStacks()) {
            rehandles += getRehandlesInStack(stack);
        }

        return rehandles;
    }

    // Returns the total number of rehandles in the block BLOCKID.
    public static int getRehandlesInBlock (String blockId) {
        ArrayList<ArrayList<Container>> block = yard.getStacks(blockId);
        int rehandles = 0;
        for (ArrayList<Container> stack : yard.getStacks(blockId)) {
            rehandles += getRehandlesInStack(stack);
        }
        return rehandles;
    }

    // Returns rehandles in STACK.
    public static int getRehandlesInStack (ArrayList<Container> stack) {
        int rehandles = 0;
        int size = stack.size();
        // Sort the ArrayList in reverse order to emulate a physical stack.
        Collections.sort(stack, new Comparator<Container>() {
            @Override
            public int compare(Container o1, Container o2) {
                return o1.getTier() - o2.getTier();
            }
        });
        // Check for a floating container and return 0 if true.
        if (size - 1 != stack.get(stack.size() - 1).getTier()) {
            //System.out.println("Floating container inside stack: " + stack.get(0).getUnitPosition().substring(6,13));
            return 0;
        }

        for (int i = size - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                // If the container underneath is not of kind Load, continue.
                if( ! stack.get(j).getKind().equals("Load")) {
                    continue;
                }
                long time1 = stack.get(i).getEstMvTime();
                long time2 = stack.get(j).getEstMvTime();
                if( time1 > time2 ){
                    rehandles += 1;
                    break;
                }
            }
        }

        //System.out.println("Stack inside " + stack.get(0).getUnitPosition().substring(6,13) + " will have " + rehandles + " rehandles.");
        //add the stack and its rehandle to a key value pair

        return rehandles;
    }
}
