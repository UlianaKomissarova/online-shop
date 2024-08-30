package aston.delivery.dto.input;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("post")
public class PostDeliveryInput extends DeliveryInput {
    private String trackNumber;
}