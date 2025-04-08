package pl.photomarketapp.photomarketapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PhotoResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("upload_date")
    private LocalDateTime uploadDate;

    @JsonProperty("user_id")
    private Long userId;

    public PhotoResponseDto(Long id, String title, String description, Double amount, String filePath, LocalDateTime uploadDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
    }
}
