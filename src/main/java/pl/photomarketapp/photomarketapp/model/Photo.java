package pl.photomarketapp.photomarketapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "file_path")
    private String filePath;

    @JsonProperty("upload_date")
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Photo(String title, String description, Double amount, String filePath, User user) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.filePath = filePath;
        this.uploadDate = LocalDateTime.now();
        this.user = user;
    }
}
