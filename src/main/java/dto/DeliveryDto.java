package dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private Integer id;
    private String address;
    private BigDecimal price;
    private Date date;
    private Integer orderId;
}