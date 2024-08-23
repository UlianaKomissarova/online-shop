package model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_deliveries")
public class PostDelivery extends Delivery {
    @Column(name = "track_number")
    private String trackNumber;
}