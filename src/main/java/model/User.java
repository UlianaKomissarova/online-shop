package model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "user_id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column
    private Date birthday;
    @Column(name = "is_vendor", nullable = false)
    private boolean isVendor;
}