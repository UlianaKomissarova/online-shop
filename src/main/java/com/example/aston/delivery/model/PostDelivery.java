package com.example.aston.delivery.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_deliveries")
public class PostDelivery extends Delivery {
    @Column(name = "track_number")
    private String trackNumber;
}