package pl.photomarketapp.photomarketapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="surname", nullable = false)
    private String surname;

    @Column(name="password")
    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "owner")
    private List<Photo> uploadedPhotos;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Photo> purchasedPhotos;

    public User(String name, String surname, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
