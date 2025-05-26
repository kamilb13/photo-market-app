package pl.photomarketapp.photomarketapp.mapper;

import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.model.Photo;

public class PhotoMapper {
    public static PhotoResponseDto mapToDto(Photo photo) {
        return new PhotoResponseDto(
                photo.getId(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getAmount(),
                photo.getFilePath().replace("uploads\\", ""),
                photo.getUploadDate(),
                photo.getOwner().getId()
        );
    }
}
