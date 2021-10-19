package com.home.practice.service;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.practice.model.AddProductRequest;
import com.home.practice.model.ProductResponse;
import com.home.practice.model.UpdateProductRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class DbService {

    private static final String PRODUCTS_TABLE = "Products";
    private final DynamoDB dynamoDB;

    private static final Logger LOG = LogManager.getLogger(DbService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DbService(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public ProductResponse putProductRequest(AddProductRequest request) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        Item item = new Item()
                .withPrimaryKey("id", uuidAsString)
                .withString("productName", request.getName())
                .withDouble("price", request.getPrice())
                .withString("imageUrl", request.getImageUrl());

        dynamoDB
                .getTable(PRODUCTS_TABLE)
                .putItem(item);

        return toProduct(item);
    }

    public ProductResponse update(
            UpdateProductRequest request
    ) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("id", request.getId())
                .withUpdateExpression("set #productName = :productName, #price = :price, #imageUrl = :imageUrl")
                .withNameMap(new NameMap()
                        .with("#productName", "productName")
                        .with("#price", "price")
                        .with("#imageUrl", "imageUrl")
                )
                .withValueMap(
                        new ValueMap()
                                .withNumber(":price", request.getPrice())
                                .withString(":productName", request.getName())
                                .withString(":imageUrl", request.getImageUrl())
                )
                .withReturnValues(ReturnValue.UPDATED_NEW);


        UpdateItemOutcome outcome = dynamoDB
                .getTable(PRODUCTS_TABLE)
                .updateItem(updateItemSpec);

        return toProduct(outcome.getItem());
    }

    private ProductResponse toProduct(Item item) {
        LOG.info("converting: ");
        LOG.info(item.asMap());
        LOG.info("converted");
        return ProductResponse
                .builder()
                .id(item.getString("id"))
                .name(item.getString("productName"))
                .price(item.getDouble("price"))
                .imageUrl(item.getString("imageUrl"))
                .build();
    }

}
