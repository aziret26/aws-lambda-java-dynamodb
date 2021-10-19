package com.home.practice.useCase;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.home.practice.model.ProductResponse;
import com.home.practice.model.UpdateProductRequest;
import com.home.practice.service.*;

public class ProductUpdateFunction implements RequestHandler<UpdateProductRequest, ProductResponse> {

    private final AwsClientFactory awsClientFactory = new AwsClientFactory();
    private final DbService dbService = new DbService(awsClientFactory.getDynamoDB());

    @Override
    public ProductResponse handleRequest(
            UpdateProductRequest request,
            Context context
    ) {
        return dbService.update(request);
    }

}