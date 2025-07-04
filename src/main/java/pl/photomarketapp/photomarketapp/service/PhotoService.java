package pl.photomarketapp.photomarketapp.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
//import pl.photomarketapp.photomarketapp.exception.PhotoUploadException;
import pl.photomarketapp.photomarketapp.enums.PhotoCategory;
import pl.photomarketapp.photomarketapp.mapper.PhotoMapper;
import pl.photomarketapp.photomarketapp.model.Photo;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.PhotoRepository;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";
    private static final String PHOTO_FORMAT = ".jpg";

    public PhotoService(PhotoRepository photoRepository, UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    public PhotoResponseDto addPhoto(PhotoRequestDto photoRequestDto) {
        File directory = new File(UPLOAD_DIR);
        if(!directory.exists()){
            directory.mkdir();
        }
        User user = userRepository.findById(photoRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        MultipartFile file = photoRequestDto.getFile();
        String fileName = System.currentTimeMillis() + "_" + user.getId() + "_" + user.getName() + "_" + user.getSurname() + PHOTO_FORMAT;
        Path path = Paths.get(UPLOAD_DIR + fileName);
        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Photo photo = new Photo(
                photoRequestDto.getTitle(),
                photoRequestDto.getDescription(),
                photoRequestDto.getAmount(),
                photoRequestDto.getCategory(),
                path.toString(),
                user
        );
        photoRepository.save(photo);
        return PhotoMapper.mapToDto(photo);
    }

    public List<PhotoResponseDto> getPhotos() {
        List<Photo> photos = photoRepository.findAll();
        return photos.stream()
                .map(PhotoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PhotoResponseDto> getUploadedPhotos(Long id) {
        List<Photo> photos = photoRepository.findByOwnerId(id);
        return photos.stream()
                .map(PhotoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PhotoResponseDto> getPurchasedPhotos(Long userId) {
        List<Photo> photos = photoRepository.findPhotosPurchasedByUserId(userId);
        return photos.stream()
                .map(PhotoMapper::mapToDto)
                .collect(Collectors.toList());
//        return Collections.emptyList();
    }

    @Transactional
    public void buyPhoto(Long userId, Long photoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        user.getPurchasedPhotos().add(photo);
        photo.getBuyers().add(user);
        userRepository.save(user);
    }
    
    public List<String> getCategories() {
        return Arrays.stream(PhotoCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public PhotoResponseDto updatePhoto(PhotoRequestDto photoRequestDto, Long photoId) {
        Photo photoFromDb = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        if (photoRequestDto.getTitle() != null && !photoRequestDto.getTitle().isEmpty())
            photoFromDb.setTitle(photoRequestDto.getTitle());
        if (photoRequestDto.getDescription() != null && !photoRequestDto.getDescription().isEmpty())
            photoFromDb.setDescription(photoRequestDto.getDescription());
        if (photoRequestDto.getAmount() != null)
            photoFromDb.setAmount(photoRequestDto.getAmount());
        if (photoRequestDto.getCategory() != null)
            photoFromDb.setCategory(photoRequestDto.getCategory());
        photoRepository.save(photoFromDb);
        return PhotoMapper.mapToDto(photoFromDb);
    }

    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }
}
