package pl.photomarketapp.photomarketapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="surname", nullable = false)
    private String surname;

    @Column(name="phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "user")
    private List<Photo> photos;
}
