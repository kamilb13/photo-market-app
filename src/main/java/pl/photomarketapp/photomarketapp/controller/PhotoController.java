package pl.photomarketapp.photomarketapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.photomarketapp.photomarketapp.dto.request.BuyPhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.enums.PhotoCategories;
import pl.photomarketapp.photomarketapp.service.PhotoService;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
public class  PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    private final Path photosDir = Paths.get("uploads");

    @GetMapping("/get-photo/{filename}")
    public ResponseEntity<UrlResource> getPhoto(@PathVariable String filename) {
        try {
            Path filePath = photosDir.resolve(filename);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                throw new FileNotFoundException("Nie znaleziono pliku: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Błąd podczas pobierania pliku", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/add-photo")
    public ResponseEntity<?> addPhoto(@ModelAttribute PhotoRequestDto photoRequestDto) {
        try {
            PhotoResponseDto photoResponseDto = photoService.addPhoto(photoRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(photoResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-photos")
    public ResponseEntity<?> getPhotos() {
        try {
            List<PhotoResponseDto> photos = photoService.getPhotos();
            return ResponseEntity.status(HttpStatus.OK).body(photos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-user-photos/{id}")
    public ResponseEntity<?> getUserPhotos(@PathVariable Long id) {
        try {
            List<PhotoResponseDto> photos = photoService.getUploadedPhotos(id);
            return ResponseEntity.status(HttpStatus.OK).body(photos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-purchased-photos/{id}")
    public ResponseEntity<?> getPurchasedPhotos(@PathVariable Long id) {
        try {
            List<PhotoResponseDto> photos = photoService.getPurchasedPhotos(id);
            return ResponseEntity.status(HttpStatus.OK).body(photos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/buy-photo")
    public ResponseEntity<?> buyPhoto(@RequestBody BuyPhotoRequestDto buyPhotoRequestDto) {
        try {
            photoService.buyPhoto(buyPhotoRequestDto.getUserId(), buyPhotoRequestDto.getPhotoId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Collections.singletonMap("status", "Photo purchased successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to buy photo: " + e.getMessage());
        }
    }
    
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            List<String> categories = photoService.getCategories();
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
