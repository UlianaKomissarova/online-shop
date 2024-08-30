package aston.delivery.dto.output;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDeliveryOutput extends DeliveryOutput {
    private String trackNumber;
}