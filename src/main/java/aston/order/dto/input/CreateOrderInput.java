package aston.order.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderInput {
    private int buyerId;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    private List<Integer> itemsIds;
    private Integer deliveryId;
}