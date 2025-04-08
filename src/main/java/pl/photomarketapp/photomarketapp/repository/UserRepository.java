package pl.photomarketapp.photomarketapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.photomarketapp.photomarketapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
