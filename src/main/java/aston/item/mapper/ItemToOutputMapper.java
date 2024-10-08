package aston.item.mapper;

import aston.item.dto.output.ItemOutput;
import aston.item.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemToOutputMapper {
    public static ItemOutput toOutputFromItem(Item item) {
        return ItemOutput.builder()
            .description(item.getDescription())
            .id(item.getId())
            .isAvailable(item.isAvailable())
            .name(item.getName())
            .vendorId(item.getVendor().getId())
            .price(item.getPrice())
            .quantity(item.getQuantity())
            .build();
    }
}