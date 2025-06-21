package pl.photomarketapp.photomarketapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.enums.PhotoCategory;
import pl.photomarketapp.photomarketapp.model.Photo;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.PhotoRepository;
import pl.photomarketapp.photomarketapp.repository.UserRepository;
import pl.photomarketapp.photomarketapp.service.PhotoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PhotoService photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        photoService = new PhotoService(photoRepository, userRepository);
    }

    @Test
    void shouldAddPhotoAndReturnResponse() {
        User user = new User();
        user.setId(1L);
        user.setName("A");
        user.setSurname("A");

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test".getBytes());

        PhotoRequestDto requestDto = new PhotoRequestDto();
        requestDto.setTitle("Test");
        requestDto.setDescription("Desc");
        requestDto.setAmount(9.99);
        requestDto.setFile(mockFile);
        requestDto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PhotoResponseDto response = photoService.addPhoto(requestDto);

        assertEquals("Test", response.getTitle());
        assertEquals("Desc", response.getDescription());
        assertEquals(9.99, response.getAmount());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void shouldReturnAllPhotos() {
        User user = new User();
        user.setId(1L);

        Photo photo = new Photo("t", "d", 9.99, PhotoCategory.CARS, "p", user);
        photo.setId(1L);
        when(photoRepository.findAll()).thenReturn(List.of(photo));

        List<PhotoResponseDto> photos = photoService.getPhotos();

        assertEquals(1, photos.size());
        assertEquals("t", photos.get(0).getTitle());
    }

    @Test
    void shouldReturnUploadedPhotos() {
        User user = new User();
        user.setId(1L);

        Photo photo = new Photo("a", "b", 9.99, PhotoCategory.CARS,"path", user);
        photo.setId(2L);
        when(photoRepository.findByOwnerId(1L)).thenReturn(List.of(photo));

        List<PhotoResponseDto> result = photoService.getUploadedPhotos(1L);

        assertEquals(1, result.size());
        assertEquals("a", result.get(0).getTitle());
    }

    @Test
    void shouldReturnPurchasedPhotos() {
        User user = new User();
        user.setId(3L);

        Photo photo = new Photo("a", "b", 9.99, PhotoCategory.CARS,"path", user);
        photo.setId(4L);
        when(photoRepository.findPhotosPurchasedByUserId(3L)).thenReturn(List.of(photo));

        List<PhotoResponseDto> result = photoService.getPurchasedPhotos(3L);

        assertEquals(1, result.size());
        assertEquals("a", result.get(0).getTitle());
    }

    @Test
    void shouldBuyPhoto() {
        User user = new User();
        user.setId(5L);
        user.setName("A");
        user.setSurname("A");

        user.setPurchasedPhotos(new ArrayList<>());

        Photo photo = new Photo();
        photo.setId(6L);

        photo.setBuyers(new ArrayList<>());

        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(photoRepository.findById(6L)).thenReturn(Optional.of(photo));

        photoService.buyPhoto(5L, 6L);

        assertTrue(user.getPurchasedPhotos().contains(photo));
        assertTrue(photo.getBuyers().contains(user));
    }
}
