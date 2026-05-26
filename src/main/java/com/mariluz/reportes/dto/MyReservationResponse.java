package com.mariluz.reportes.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class MyReservationResponse {
    private Integer id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}