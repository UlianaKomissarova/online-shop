package aston.order.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderOutput {
    private int id;
    private int buyerId;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    private List<Integer> itemsIds;
    private Integer deliveryId;
}