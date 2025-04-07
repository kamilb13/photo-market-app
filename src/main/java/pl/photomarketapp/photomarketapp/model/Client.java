package pl.photomarketapp.photomarketapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="clients")
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @Column(name="name", nullable = false, unique = false)
    private String name;

    @Column(name="surname", nullable = false, unique = false)
    private String surname;

    @Column(name="phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="created_at")
    private Date createdAt;
}
