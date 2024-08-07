package dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private int id;
    private int itemId;
    private int buyerId;
    private String status;
    private Date createdAt;
}