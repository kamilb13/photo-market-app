package pl.photomarketapp.photomarketapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.photomarketapp.photomarketapp.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUserId(Long userId);
}
