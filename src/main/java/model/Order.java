package model;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id;
    private int itemId;
    private int buyerId;
    private OrderStatus status;
    private Date createdAt;
}