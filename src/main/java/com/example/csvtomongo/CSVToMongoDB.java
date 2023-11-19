package com.example.csvtomongo;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVToMongoDB {

    public static void main(String[] args) {
        String csvFile = "src/main/resources/rooms.csv"; // Replace with the path to your CSV file
        String line;
        String csvSplitBy = ","; // Use the appropriate delimiter

        try {
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("booking");
            MongoCollection<Document> collection = database.getCollection("rooms");

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(csvSplitBy);

                    // Create a document and insert it into MongoDB
                    Document doc = new Document("name", data[0])
                            .append("description", data[1])
                            .append("neighborhoodOverview", data[2])
                            .append("location", data[3])
                            .append("about", data[4])
                            .append("neighbourhood", data[5])
                            .append("type", data[6])
                            .append("price", data[7])
                            .append("reviewsPerMonth", data[8]);
                    collection.insertOne(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Data loaded into MongoDB 'rooms' collection.");
    }
}

