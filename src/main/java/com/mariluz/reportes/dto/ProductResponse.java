package com.mariluz.reportes.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private Integer price;
    private Integer quantity;
}