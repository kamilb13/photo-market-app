package pl.photomarketapp.photomarketapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.photomarketapp.photomarketapp.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
