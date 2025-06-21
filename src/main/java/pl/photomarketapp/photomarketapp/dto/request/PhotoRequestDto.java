package pl.photomarketapp.photomarketapp.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import pl.photomarketapp.photomarketapp.enums.PhotoCategories;

@Data
public class PhotoRequestDto {
    private String title;
    private String description;
    private Double amount;
    private PhotoCategories category;
    private Long userId;
    private MultipartFile file;
}
