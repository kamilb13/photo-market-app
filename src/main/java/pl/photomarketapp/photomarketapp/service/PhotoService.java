package pl.photomarketapp.photomarketapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.photomarketapp.photomarketapp.dto.request.PhotoRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.exception.PhotoUploadException;
import pl.photomarketapp.photomarketapp.mapper.PhotoMapper;
import pl.photomarketapp.photomarketapp.mapper.UserMapper;
import pl.photomarketapp.photomarketapp.model.Photo;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.PhotoRepository;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
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

    public PhotoResponseDto addPhoto(PhotoRequestDto photoRequestDto) throws PhotoUploadException {
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
            throw new PhotoUploadException("Failed to save photo file", e);
        }
        Photo photo = new Photo(
                photoRequestDto.getTitle(),
                photoRequestDto.getDescription(),
                photoRequestDto.getAmount(),
                path.toString(),
                user
        );
        photoRepository.save(photo);
        return new PhotoResponseDto(
                photo.getId(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getAmount(),
                photo.getFilePath(),
                photo.getUploadDate(),
                photo.getUser().getId()
        );
    }

    public List<PhotoResponseDto> getPhotos() {
        List<Photo> photos = photoRepository.findAll();
        return photos.stream()
                .map(PhotoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
