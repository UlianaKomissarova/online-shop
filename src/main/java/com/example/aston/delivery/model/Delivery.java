package com.example.aston.delivery.model;

import com.example.aston.order.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id", nullable = false)
    private Integer id;
    @Column
    private String address;
    @Column
    private BigDecimal price;
    @Column(name = "delivered_on")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;
}