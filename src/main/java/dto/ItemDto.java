package dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private boolean isAvailable;
    private int vendorId;
}