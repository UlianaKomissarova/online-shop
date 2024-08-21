package model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "delivery_seq", sequenceName = "delivery_sequence", allocationSize = 1)
public abstract class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq")
    @Column(name = "delivery_id", nullable = false)
    private Integer id;
    @Column
    private String address;
    @Column
    private BigDecimal price;
    @Column(name = "delivered_on")
    private Date date;
    @OneToOne
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
}