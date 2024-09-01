package com.example.aston.order.dto.input;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderInput {
    private int buyerId;
    private String status;
    private List<Integer> itemsIds;
}