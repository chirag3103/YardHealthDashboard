package com.navis.dashboard;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.sql.ResultSet;
import java.util.List;

/*
Gets the data from N4 and puts it in a data structure for processing by business logic.
 */
public interface DataStoreConnection {

    // Returns a Document from WorkInstruction.
    List<Document> selectWorkInstructions();

    // Returns a Document from UnitDescriptions.
    List<Document> selectUnitDescriptions();

    //FindIterable<ResultSet> abc();


}
