package com.navis.dashboard;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.hibernate.result.ResultSetOutput;

import javax.print.Doc;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MySQLConnection implements DataStoreConnection {


    static Statement statement;
    static Connection connection;
    static ResultSet resultSet;
    List<Document> listDocument;

    MySQLConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/YardDashboard", "root", "1691");
            statement = connection.createStatement();
            System.out.println("Success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    @Override
    public List<Document> selectWorkInstructions() {

        //select Unit Nbr, Unit Position, Unit T-State, Est Mv Time from workinstructions where Unit T-State = Yard

        listDocument = new ArrayList<>();
        Document document;
        try {
            resultSet = statement.executeQuery("Select `Unit Nbr`,`Unit Position`, `Unit T-State`,`Est Mv Time`,`Kind` from workinstructions where `Unit T-State`='Yard'");
            while (resultSet.next())
            {
                String unitNbr = resultSet.getString("Unit Nbr");
                String unitPosition = resultSet.getString("Unit Position");
                String unitMvTime = resultSet.getString("Est Mv Time");
                String kind = resultSet.getString("Kind");
                String unitTState=resultSet.getString("Unit T-State");

                //date format is "11Dec2018 21:13";
                if(!unitMvTime.equals("")){
                    SimpleDateFormat df = new SimpleDateFormat("ddMMMyy HH:mm");
                    Date date =df.parse(unitMvTime);
                    //System.out.println(date);
                    unitMvTime=(new SimpleDateFormat("MM/dd/yy HH:mm")).format(date);
                }


                document = new Document("Unit T-State", unitTState)
                        .append("Kind",kind)
                        .append("Unit Nbr", unitNbr)
                        .append("Unit Position", unitPosition)
                        .append("Est Mv Time", unitMvTime);


                listDocument.add(document);

            }
            //collection.insertMany(listDocument);
            //FindIterable<Document> findIterable= (FindIterable<Document>) document;

            //select
            // Unit Nbr, Unit Position, Unit T-State, Est Mv Time

            //where
            //Unit T-State = Yard

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listDocument;
    }

    @Override
    public List<Document> selectUnitDescriptions() {
        //select Unit Nbr, Unit Position, Unit T-State, Est Mv Time from workinstructions where Unit T-State = Yard

        listDocument = new ArrayList<>();
        Document document;
        try {
            resultSet = statement.executeQuery("Select `Unit Nbr`,`Position`, `T-State` from unitdescriptions where `T-State`='Yard'");
            while (resultSet.next())
            {
                String unitNbr = resultSet.getString("Unit Nbr");
                String unitPosition = resultSet.getString("Position");
                String unitTState=resultSet.getString("T-State");

                document = new Document("T-State", unitTState)
                        .append("Unit Nbr", unitNbr)
                        .append("Position", unitPosition);

                listDocument.add(document);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listDocument;
    }

}