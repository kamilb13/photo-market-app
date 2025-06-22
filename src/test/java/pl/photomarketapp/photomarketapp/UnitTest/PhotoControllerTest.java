package pl.photomarketapp.photomarketapp.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.photomarketapp.photomarketapp.controller.PhotoController;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.enums.PhotoCategory;
import pl.photomarketapp.photomarketapp.service.PhotoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoControllerTest {

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private PhotoController photoController;

    @Test
    void shouldAddPhotoAndReturnResponse() {
        PhotoRequestDto requestDto = new PhotoRequestDto();

        PhotoResponseDto responseDto = new PhotoResponseDto(
                1L, "test", "desc", 99.99, PhotoCategory.CARS,"uploads/photo.jpg", LocalDateTime.now(), 1L, "username"
        );

        when(photoService.addPhoto(requestDto)).thenReturn(responseDto);

        ResponseEntity<?> response = photoController.addPhoto(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof PhotoResponseDto);
        assertEquals("test", ((PhotoResponseDto) response.getBody()).getTitle());
    }

    @Test
    void shouldReturnAllPhotos() {
        List<PhotoResponseDto> photos = List.of(
                new PhotoResponseDto(1L, "1", "desc 1", 50.0, PhotoCategory.CARS,"uploads/p1.jpg", LocalDateTime.now(), 1L, "username1"),
                new PhotoResponseDto(2L, "2", "desc 2", 70.0, PhotoCategory.CARS,"uploads/p2.jpg", LocalDateTime.now(), 2L, "username2")
        );

        when(photoService.getPhotos()).thenReturn(photos);

        ResponseEntity<?> response = photoController.getPhotos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    @Test
    void shouldReturnPhotosOfSpecificUser() {
        Long userId = 1L;

        List<PhotoResponseDto> userPhotos = List.of(
                new PhotoResponseDto(3L, "test", "desc", 120.0, PhotoCategory.CARS,"uploads/portret.jpg", LocalDateTime.now(), userId, "username")
        );

        when(photoService.getUploadedPhotos(userId)).thenReturn(userPhotos);

        ResponseEntity<?> response = photoController.getUserPhotos(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals("test", ((PhotoResponseDto) ((List<?>) response.getBody()).get(0)).getTitle());
    }

}