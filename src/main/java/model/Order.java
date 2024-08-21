package model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", allocationSize = 1)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @Column(name = "order_id", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "buyer_id")
    private User buyer;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "created")
    private Date createdAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
        name = "orders_items",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;
    @OneToOne(mappedBy = "order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Delivery delivery;
}