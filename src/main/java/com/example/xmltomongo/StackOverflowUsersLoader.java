package com.example.xmltomongo;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.bson.Document;

import java.io.File;

public class StackOverflowUsersLoader {

    public static void main(String[] args) {
        try {
            File file = new File("src/main/resources/users.xml"); // Replace with the path to your XML file
            JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Reading data from the XML file and converting it into Java objects
            Users users = (Users) jaxbUnmarshaller.unmarshal(file);

            // Connecting to MongoDB
            String connectionString = "mongodb://localhost:27017"; // Replace with your MongoDB connection string

            try {
                MongoClient mongoClient = MongoClients.create(connectionString);
                MongoDatabase clients = mongoClient.getDatabase("booking");
                MongoCollection<Document> collection = clients.getCollection("clients");

                // Writing user data to MongoDB
                for (User user : users.getUserList()) {
                    Document doc = new Document()
                            .append("Name", user.getName())
                            .append("CreationDate", user.getCreationDate());
                    collection.insertOne(doc);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            System.out.println("Data written to MongoDB 'clients' collection.");

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
