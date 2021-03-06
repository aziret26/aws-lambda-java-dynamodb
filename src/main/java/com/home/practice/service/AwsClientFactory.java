package com.home.practice.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Just an util class for an eager initialization of sdk clients.
 */
public class AwsClientFactory {

    private static final Logger LOG = LogManager.getLogger(AwsClientFactory.class);

    private final DynamoDB dynamoDB;

    /**
     * AWS regions should be env variables if you want to generalize the solution.
     */
    public AwsClientFactory() {
        LOG.debug("AWS clients factory initialization.");

        AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_2)
                .build();
        dynamoDB = new DynamoDB(dynamoDBClient);
    }

    public DynamoDB getDynamoDB() {
        return dynamoDB;
    }

}