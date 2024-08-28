package aston.delivery.dto.input;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CourierDeliveryInput.class, name = "courier"),
    @JsonSubTypes.Type(value = ParcelLockerDeliveryInput.class, name = "parcel_locker"),
    @JsonSubTypes.Type(value = PostDeliveryInput.class, name = "post")
})
public class DeliveryInput {
    private String type;
    private Integer id;
    private String address;
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Integer orderId;
}