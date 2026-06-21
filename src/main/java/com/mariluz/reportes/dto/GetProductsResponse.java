package com.mariluz.reportes.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetProductsResponse {
    private List<ProductResponse> products;
}