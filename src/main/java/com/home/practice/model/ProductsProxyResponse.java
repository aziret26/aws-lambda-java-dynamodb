package com.home.practice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsProxyResponse {

    private int statusCode;
    private Map<String, String> headers;
    private String body;
    @JsonProperty("isBase64Encoded")
    @Builder.Default
    private boolean isBase64Encoded = false;

}
