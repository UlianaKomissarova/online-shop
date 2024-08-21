package dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDeliveryDto extends DeliveryDto {
    private String trackNumber;
}