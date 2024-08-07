package service.mapper;

import dto.ItemDto;
import model.Item;

public class ItemMapper {
    public static Item toItemFromItemDto(ItemDto dto) {
        return Item.builder()
            .id(dto.getId())
            .vendorId(dto.getVendorId())
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
            .vendorId(item.getVendorId())
            .build();
    }
}