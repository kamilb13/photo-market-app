package pl.photomarketapp.photomarketapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.photomarketapp.photomarketapp.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
