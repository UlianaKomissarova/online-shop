package aston.order.mapper;

import aston.item.model.Item;
import aston.order.dto.output.OrderOutput;
import aston.order.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderToOutputMapper {
    public static OrderOutput toDtoFromOrder(Order order) {
        OrderOutput dto = OrderOutput.builder()
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