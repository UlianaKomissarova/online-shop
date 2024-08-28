package aston.item.dto.input;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemInput {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private Boolean isAvailable;
}