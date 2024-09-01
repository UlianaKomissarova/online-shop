package com.example.aston.item.dto.input;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemInput {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private boolean isAvailable;
    private int vendorId;
}