package pl.photomarketapp.photomarketapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.service.PhotoService;

@RestController
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("add-photo")
    public ResponseEntity<?> addPhoto(@ModelAttribute PhotoRequestDto photoRequestDto) {
        try {
            PhotoResponseDto photoResponseDto = photoService.addPhoto(photoRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(photoResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
