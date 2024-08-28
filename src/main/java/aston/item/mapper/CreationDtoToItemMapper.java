package aston.item.mapper;

import aston.item.dto.input.CreateItemInput;
import aston.item.model.Item;
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