package aston.order.mapper;

import aston.order.dto.input.CreateOrderInput;
import aston.order.model.*;
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