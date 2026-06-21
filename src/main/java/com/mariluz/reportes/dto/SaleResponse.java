package com.mariluz.reportes.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SaleResponse {
    private Integer id;
    private Integer total;
    private String status; 
    private LocalDateTime createdAt;
    private List<SaleItemResponse> products;
}