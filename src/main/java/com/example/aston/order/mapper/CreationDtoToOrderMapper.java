package com.example.aston.order.mapper;

import com.example.aston.order.dto.input.CreateOrderInput;
import com.example.aston.order.model.Order;
import com.example.aston.order.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class CreationDtoToOrderMapper {
    public static Order toOrderFromCreationDto(CreateOrderInput dto) {
        return Order.builder()
            .status(OrderStatus.valueOf(dto.getStatus()))
            .createdAt(dto.getCreatedAt())
            .build();
    }
}