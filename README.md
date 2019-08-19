# YardHealthDashboard
A yard health dashboard developed as a part of internship project.

This is a unique code written to support both NoSQL as well as MySQL without any change in the code.
This code includes a special algorithm developed to calculate the number of rehandles required for fetching a given container from any given yard location.

MongoConnection.java
MongoDB: We are reading data from two database collections WorkInstructions and UnitDescriptions.

MySQLConnection.java
MySQL: We are reading data from two database tables called WorkInstructions and UnitDescriptions.

The return type from both these classes is mapped to a List<Document> with the help of which using both the database without much change is possible.
  
BusinessLogic.java
Consists of the algorithm to calculate the number of rehandles given any container in the yard.
This file returns a json which consists of yard analytics which can be used by any UI to visualize this yard analytics data.

Yard.java
The constructor for this class consists the lines of code which help you choose the type of db you wanna use.
