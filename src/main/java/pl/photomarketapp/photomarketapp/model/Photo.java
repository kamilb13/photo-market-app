package pl.photomarketapp.photomarketapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.photomarketapp.photomarketapp.enums.PhotoCategory;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "photos")
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name="category")
    private PhotoCategory category;

    @Column(name = "file_path")
    private String filePath;

    @JsonProperty("upload_date")
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToMany(mappedBy = "purchasedPhotos")
    private List<User> buyers;

    public Photo(String title, String description, Double amount, PhotoCategory category, String filePath, User user) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.filePath = filePath;
        this.uploadDate = LocalDateTime.now();
        this.owner = user;
    }
}
