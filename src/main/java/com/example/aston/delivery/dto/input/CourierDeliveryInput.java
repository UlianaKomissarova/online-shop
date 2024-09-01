package com.example.aston.delivery.dto.input;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("courier")
public class CourierDeliveryInput extends DeliveryInput {
    @JsonFormat(pattern = "HH:mm")
    private LocalTime deliverFrom;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime deliverTo;
    private String courierPhone;
}