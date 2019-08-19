package com.navis.dashboard;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import org.bson.Document;

import javax.print.Doc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MongoConnection implements DataStoreConnection  {

    static MongoClient mongoClient;
    static MongoDatabase mongoDatabase;
    static MongoCollection<Document> mongoUnitDescriptions;
    static MongoCollection<Document> mongoWorkInstructions;
    static BasicDBObject yardQuery;
    static BasicDBObject displayFields;
    static List<Document> listDocument;




    // Name of the N4 database.
    static String dataBase = "config";

    MongoConnection() {
        String host = "localhost";
        int port = 27017;
        String dataBase = "config";
        mongoClient = new MongoClient(host, port);
        mongoDatabase = mongoClient.getDatabase(dataBase);
        // Selecting the Work_Instructions collection.
        mongoWorkInstructions = mongoDatabase.getCollection("Work_Instructions");
        //Selecting the Unit_Descriptions collection.
        mongoUnitDescriptions = mongoDatabase.getCollection("Unit_Descriptions");
    }

    public static void mongo_collection_print(FindIterable<Document> findIterable) {
        //FindIterable<Document> iterDoc = mongoCollection.find();
        int total_doc_count = 1;
        // Getting the iterator
        Iterator it = findIterable.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            total_doc_count++;
        }
        System.out.println("total documents are " + total_doc_count);
    }

    public List<Document> selectUnitDescriptions() {

        listDocument = new ArrayList<>();


        // Creating the where query
        yardQuery = new BasicDBObject();
        yardQuery.put("T-State", "Yard");
        //yardQuery.put("Category", "Export");

        // Selecting fields to project
        displayFields = new BasicDBObject();
        displayFields.put("_id", 0);
        displayFields.put("T-State", 1);
        displayFields.put("Unit Nbr", 1);
        displayFields.put("Position", 1);
        displayFields.put("Est Mv Time", 1);

        //Aggregating the where query with projection
        //return mongoUnitDescriptions.find(yardQuery).projection(displayFields);
        FindIterable<Document> iterable=mongoUnitDescriptions.find(yardQuery).projection(displayFields);
        //ResultSet resultSet=(ResultSet) mongoUnitDescriptions.find(yardQuery).projection(displayFields);

        for(Document doc:iterable){
            listDocument.add(doc);
        }

        return listDocument;
        //return resultSet;
    }

    public List<Document> selectWorkInstructions() {

        listDocument = new ArrayList<>();

        yardQuery = new BasicDBObject();
        yardQuery.put("Unit T-State", "Yard");
        //yardQuery.put("Kind", "Load");
        //yardQuery.put("Stage", "Planned");

        // Selecting fields to project
        displayFields = new BasicDBObject();
        displayFields.put("_id", 0);
        displayFields.put("Unit T-State", 1);
        displayFields.put("Kind", 1);
        displayFields.put("Unit Nbr", 1);
        displayFields.put("Move #", 1);
        displayFields.put("Unit Position", 1);
        displayFields.put("Est Mv Time", 1);

        FindIterable<Document> iterable=mongoWorkInstructions.find(yardQuery).projection(displayFields);


        for(Document doc:iterable){
            listDocument.add(doc);
        }





       return listDocument;

    }







}
