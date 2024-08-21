package model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
@SequenceGenerator(name = "item_seq", sequenceName = "item_sequence", allocationSize = 1)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @Column(name = "item_id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User vendor;
}