package com.home.practice.useCase;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.practice.exception.ProductsLambdaClientException;
import com.home.practice.model.Product;
import com.home.practice.model.ProductsProxyResponse;
import com.home.practice.model.UpdateProductRequest;
import com.home.practice.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.lang.Boolean.FALSE;

/**
 * Request ad response types can be Input and Output stream or any custom objects.
 * Using streams directly helps to avoid different handling
 * when executing Lambda locally and in AWS environment.
 */
public class ProductUpdateFunction implements RequestHandler<AwsProxyRequest, ProductsProxyResponse> {

    // Use static variables to keep a context between executions
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AwsClientFactory awsClientFactory = new AwsClientFactory();
    private final DbService dbService = new DbService(awsClientFactory.getDynamoDB());
    private final ResponseService responseService = new ResponseService(objectMapper);
    private final RequestService requestService = new RequestService(objectMapper);
    private final PropertyStorage propertyStorage = new PropertyStorage();

    // Use custom logger or logger from a Lambda context by
    // calling context.getLogger()
    private static final Logger LOG = LogManager.getLogger(ProductUpdateFunction.class);

    /**
     * Send Product form data to a specific email
     * and put the form data into database.
     * <p>
     *
     * @param request API Gateway request
     * @param context Lambda context
     * @return Proxy response to API Gateway
     */
    @Override
    public ProductsProxyResponse handleRequest(AwsProxyRequest request, Context context) {
        try {
            LOG.info("Request received.");
            LOG.debug(requestService.getAsPrettyString(request));

            return isWarmUpRequest(request)
                    ? handleWarmUpRequest()
                    : update(request);
        } catch (ProductsLambdaClientException e) {
            LOG.error("Request was not handled due to a client error.", e);
            return responseService.buildResponse(400, "Client error.");
        } catch (Exception e) {
            LOG.error("Request was not handled due to a server error.", e);
            return responseService.buildResponse(500, "Server error.");
        }
    }

    private ProductsProxyResponse update(AwsProxyRequest request) {
        // Parsing an input request
        UpdateProductRequest productRequest = requestService.getUpdateProductRequest(request);


        // Saving request to DB
        Product product = dbService.update(productRequest);
        LOG.info("ProductRequest has been written to DB.");

        return responseService.buildResponse(200, product);
    }

    private Boolean isWarmUpRequest(AwsProxyRequest request) {
        return Optional.ofNullable(request.getMultiValueHeaders())
                .map(headers -> headers.containsKey("X-WARM-UP"))
                .orElse(FALSE);
    }

    /**
     * Just to load a classpath and initialize all static fields for next calls.
     * Skips real AWS connections.
     *
     * @return Stub response
     */
    private ProductsProxyResponse handleWarmUpRequest() {
        LOG.info("Lambda was warmed up.");
        return responseService.buildWarmUpResponse();
    }

}