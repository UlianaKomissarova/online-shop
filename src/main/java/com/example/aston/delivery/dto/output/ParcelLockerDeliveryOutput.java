package com.example.aston.delivery.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelLockerDeliveryOutput extends DeliveryOutput {
    private int boxNumber;
    private String pin;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openFrom;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closedFrom;
}