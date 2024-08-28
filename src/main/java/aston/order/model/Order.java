package aston.order.model;

import aston.delivery.model.Delivery;
import aston.item.model.Item;
import aston.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "orders_items",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;
}