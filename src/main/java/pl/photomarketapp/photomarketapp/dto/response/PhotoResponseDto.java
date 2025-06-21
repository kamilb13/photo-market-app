package pl.photomarketapp.photomarketapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.photomarketapp.photomarketapp.enums.PhotoCategories;

import java.time.LocalDateTime;

@Data
public class PhotoResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("amount")
    private Double amount;
    
    @JsonProperty("category")
    private PhotoCategories category;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("upload_date")
    private LocalDateTime uploadDate;

    @JsonProperty("owner_id")
    private Long userId;
    
    @JsonProperty("owner_username")
    private String username;

    public PhotoResponseDto(Long id, String title, String description, Double amount, PhotoCategories category, String filePath, LocalDateTime uploadDate, Long userId, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.userId = userId;
        this.username = username;
    }
}
