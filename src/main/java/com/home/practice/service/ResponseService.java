package com.home.practice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.practice.exception.ProductsLambdaServerException;
import com.home.practice.model.Product;
import com.home.practice.model.ProductsResponseBody;
import com.home.practice.model.ProductsProxyResponse;
import org.apache.http.entity.ContentType;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class ResponseService {

    private final ObjectMapper objectMapper;

    public ResponseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProductsProxyResponse buildWarmUpResponse() {
        return buildResponse(201, "Lambda was warmed up. V2");
    }

    public ProductsProxyResponse buildResponse(int statusCode, String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        headers.put("Access-Control-Allow-Origin", "*");
        return ProductsProxyResponse.builder()
                .statusCode(statusCode)
                .headers(headers)
                .body(getBodyAsString(body))
                .build();
    }

    public ProductsProxyResponse buildResponse(int statusCode, Product body) {
        String bodyAsString = getBodyAsString(body);

        return buildResponse(statusCode, bodyAsString);
    }

    private String getBodyAsString(Object body) {
        try {
            return objectMapper.writeValueAsString(new ProductsResponseBody(body));
        } catch (JsonProcessingException e) {
            throw new ProductsLambdaServerException("Writing ProductResponseBody as string failed.", e);
        }
    }
}

