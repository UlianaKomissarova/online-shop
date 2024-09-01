package com.example.aston.item.mapper;

import com.example.aston.item.dto.input.CreateItemInput;
import com.example.aston.item.model.Item;
import org.springframework.stereotype.Component;

@Component
public class CreationDtoToItemMapper {
    public static Item toItemFromCreationDto(CreateItemInput dto) {
        return Item.builder()
            .price(dto.getPrice())
            .quantity(dto.getQuantity())
            .name(dto.getName())
            .description(dto.getDescription())
            .isAvailable(dto.isAvailable())
            .build();
    }
}