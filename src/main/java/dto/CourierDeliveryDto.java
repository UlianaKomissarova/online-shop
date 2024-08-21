package dto;

import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierDeliveryDto extends DeliveryDto {
    private LocalTime deliverFrom;
    private LocalTime deliverTo;
    private String courierPhone;
}