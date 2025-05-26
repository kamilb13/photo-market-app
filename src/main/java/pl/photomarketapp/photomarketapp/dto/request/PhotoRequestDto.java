package pl.photomarketapp.photomarketapp.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoRequestDto {
    private String title;
    private String description;
    private Double amount;
    private Long userId;
    private MultipartFile file;
}
