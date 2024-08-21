package service.mapper;

import dto.ItemDto;
import model.Item;

public class ItemMapper {
    public static Item toItemFromDto(ItemDto dto) {
        return Item.builder()
            .id(dto.getId())
            .price(dto.getPrice())
            .quantity(dto.getQuantity())
            .name(dto.getName())
            .description(dto.getDescription())
            .isAvailable(dto.isAvailable())
            .build();
    }

    public static ItemDto toDtoFromItem(Item item) {
        return ItemDto.builder()
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