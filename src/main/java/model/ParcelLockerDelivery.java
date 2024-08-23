package model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parcel_locker_deliveries")
public class ParcelLockerDelivery extends Delivery {
    @Column(name = "box_number")
    private int boxNumber;
    @Column
    private String pin;
    @Column(name = "open_from")
    private LocalTime openFrom;
    @Column(name = "closed_from")
    private LocalTime closedFrom;
}