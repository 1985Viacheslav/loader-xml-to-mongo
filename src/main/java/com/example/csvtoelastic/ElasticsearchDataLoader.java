package com.example.csvtoelastic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class ElasticsearchDataLoader {

    public static void main(String[] args) {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")))) {
            String csvFile = "src/main/resources/rooms.csv";
            String line;
            String cvsSplitBy = ",";
            BulkRequest bulkRequest = new BulkRequest();

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);

                    if (data.length >= 9) { // Ensure all fields are present in the CSV line
                        IndexRequest indexRequest = new IndexRequest("booking")
                                .source("name", data[0],
                                        "description", data[1],
                                        "neighborhoodOverview", data[2],
                                        "location", data[3],
                                        "about", data[4],
                                        "neighbourhood", data[5],
                                        "type", data[6],
                                        "price", data[7],
                                        "reviewsPerMonth", data[8])
                                .id(UUID.randomUUID().toString());

                        bulkRequest.add(indexRequest);
                    } else {
                        System.out.println("Incomplete data in CSV line: " + line);
                    }
                }
            }

            if (!bulkRequest.requests().isEmpty()) {
                BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {
                    System.out.println("Bulk request has failures");
                } else {
                    System.out.println("Bulk request successfully executed");
                }
            } else {
                System.out.println("No valid data to index");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}