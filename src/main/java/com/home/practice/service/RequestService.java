package com.home.practice.service;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.practice.exception.ProductsLambdaClientException;
import com.home.practice.model.AddProductRequest;
import com.home.practice.model.UpdateProductRequest;

import java.io.IOException;

public class RequestService {

    private final ObjectMapper objectMapper;

    public RequestService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AddProductRequest getAddProductRequest(AwsProxyRequest request) {
        try {
            return objectMapper.readValue(request.getBody(), AddProductRequest.class);
        } catch (IOException e) {
            throw new ProductsLambdaClientException("Product request deserialization failed.", e);
        }
    }

    public UpdateProductRequest getUpdateProductRequest(AwsProxyRequest request) {
        try {
            return objectMapper.readValue(request.getBody(), UpdateProductRequest.class);
        } catch (IOException e) {
            throw new ProductsLambdaClientException("Product request deserialization failed.", e);
        }
    }

    public String getAsPrettyString(AwsProxyRequest request) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new ProductsLambdaClientException("Writing AwsProxyRequest as String failed.", e);
        }
    }

}
