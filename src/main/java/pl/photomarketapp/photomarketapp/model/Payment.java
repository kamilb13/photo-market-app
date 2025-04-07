package pl.photomarketapp.photomarketapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "amount")
    private Double amount;
}
