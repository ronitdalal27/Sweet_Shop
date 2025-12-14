package com.example.sweetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SweetResponseDTO {

    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private int quantity;
}
