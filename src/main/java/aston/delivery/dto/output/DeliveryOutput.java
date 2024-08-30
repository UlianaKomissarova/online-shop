package aston.delivery.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOutput {
    private Integer id;
    private String address;
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Integer orderId;
}