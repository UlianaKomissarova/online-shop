package dto;

import lombok.*;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private int id;
    private int buyerId;
    private String status;
    private Date createdAt;
    private List<Integer> itemsIds;
    private Integer deliveryId;
}