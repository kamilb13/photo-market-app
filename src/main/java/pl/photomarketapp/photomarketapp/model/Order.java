package pl.photomarketapp.photomarketapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "session_id")
    private String sessionId;

    public Order(Date createdAt, Long productId, String sessionId) {
        this.createdAt = createdAt;
        this.productId = productId;
        this.sessionId = sessionId;
    }
}

