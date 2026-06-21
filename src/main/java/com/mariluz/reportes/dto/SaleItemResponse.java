package com.mariluz.reportes.dto;

import lombok.Data;

@Data
public class SaleItemResponse {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Integer subTotal;
}