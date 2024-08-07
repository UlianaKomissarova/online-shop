package dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private boolean isAvailable;
    private int vendorId;
}