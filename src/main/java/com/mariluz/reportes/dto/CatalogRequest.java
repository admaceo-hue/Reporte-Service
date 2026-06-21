package com.mariluz.reportes.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CatalogRequest {
    private List<Integer> ids;
}