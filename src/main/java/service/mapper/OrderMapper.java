package service.mapper;

import dto.OrderDto;
import model.*;

public class OrderMapper {
    public static Order toOrderFromDto(OrderDto dto) {
        return Order.builder()
            .status(OrderStatus.valueOf(dto.getStatus()))
            .id(dto.getId())
            .buyerId(dto.getBuyerId())
            .createdAt(dto.getCreatedAt())
            .itemId(dto.getItemId())
            .build();
    }

    public static OrderDto toDtoFromOrder(Order order) {
        return OrderDto.builder()
            .itemId(order.getItemId())
            .createdAt(order.getCreatedAt())
            .buyerId(order.getBuyerId())
            .id(order.getId())
            .status(order.getStatus().toString())
            .build();
    }
}