package pl.photomarketapp.photomarketapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.service.PhotoService;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            return  ResponseEntity.status(HttpStatus.OK).body(photos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
