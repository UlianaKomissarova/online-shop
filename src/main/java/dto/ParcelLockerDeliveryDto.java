package dto;

import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelLockerDeliveryDto extends DeliveryDto {
    private int boxNumber;
    private String pin;
    private LocalTime openFrom;
    private LocalTime closedFrom;
}