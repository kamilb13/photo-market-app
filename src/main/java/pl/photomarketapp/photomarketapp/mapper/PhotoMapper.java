package pl.photomarketapp.photomarketapp.mapper;

import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.model.Photo;

public class PhotoMapper {
    public static PhotoResponseDto mapToDto(Photo photo) {
        String cleanedPath = photo.getFilePath()
                .replace("uploads" + java.io.File.separator, "")
                .replace("uploads/", "");

        return new PhotoResponseDto(
                photo.getId(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getAmount(),
                cleanedPath,
                photo.getUploadDate(),
                photo.getOwner().getId()
        );
    }
}
