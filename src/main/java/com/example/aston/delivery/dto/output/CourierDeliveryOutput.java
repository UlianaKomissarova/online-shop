package com.example.aston.delivery.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierDeliveryOutput extends DeliveryOutput {
    @JsonFormat(pattern = "HH:mm")
    private LocalTime deliverFrom;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime deliverTo;
    private String courierPhone;
}