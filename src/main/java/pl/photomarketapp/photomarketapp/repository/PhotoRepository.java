package pl.photomarketapp.photomarketapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.photomarketapp.photomarketapp.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByOwnerId(Long userId);

    @Query("SELECT p FROM Photo p JOIN p.buyers b WHERE b.id = :userId")
    List<Photo> findPhotosPurchasedByUserId(@Param("userId") Long userId);
}
