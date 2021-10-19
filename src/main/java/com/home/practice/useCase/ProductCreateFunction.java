package com.home.practice.useCase;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.home.practice.model.ProductResponse;
import com.home.practice.model.AddProductRequest;
import com.home.practice.service.AwsClientFactory;
import com.home.practice.service.DbService;

public class ProductCreateFunction implements RequestHandler<AddProductRequest, ProductResponse> {

    private final AwsClientFactory awsClientFactory = new AwsClientFactory();
    private final DbService dbService = new DbService(awsClientFactory.getDynamoDB());

    @Override
    public ProductResponse handleRequest(
            AddProductRequest request,
            Context context
    ) {
        return dbService.putProductRequest(request);
    }


}