package pl.photomarketapp.photomarketapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.photomarketapp.photomarketapp.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
