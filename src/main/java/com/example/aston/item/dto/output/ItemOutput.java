package com.example.aston.item.dto.output;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOutput {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private boolean isAvailable;
    private int vendorId;
}