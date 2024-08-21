package service.mapper;

import dto.OrderDto;
import model.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static Order toOrderFromDto(OrderDto dto) {
        return Order.builder()
            .status(OrderStatus.valueOf(dto.getStatus()))
            .id(dto.getId())
            .createdAt(dto.getCreatedAt())
            .build();
    }

    public static OrderDto toDtoFromOrder(Order order) {
        OrderDto dto = OrderDto.builder()
            .createdAt(order.getCreatedAt())
            .buyerId(order.getBuyer().getId())
            .id(order.getId())
            .status(order.getStatus().toString())
            .build();

        if (order.getDelivery() != null) {
            dto.setDeliveryId(order.getDelivery().getId());
        }

        List<Integer> itemsIds = order.getItems().stream()
            .map(Item::getId)
            .collect(Collectors.toList());
        dto.setItemsIds(itemsIds);

        return dto;
    }
}