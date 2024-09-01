package com.example.aston.delivery.dto.input;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("parcel_locker")
public class ParcelLockerDeliveryInput extends DeliveryInput {
    private int boxNumber;
    private String pin;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openFrom;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closedFrom;
}