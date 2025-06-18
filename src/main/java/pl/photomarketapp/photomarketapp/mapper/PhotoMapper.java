package pl.photomarketapp.photomarketapp.mapper;

import pl.photomarketapp.photomarketapp.dto.response.PhotoResponseDto;
import pl.photomarketapp.photomarketapp.model.Photo;
import pl.photomarketapp.photomarketapp.model.User;

public class PhotoMapper {
    public static PhotoResponseDto mapToDto(Photo photo) {
        String cleanedPath = photo.getFilePath()
                .replace("uploads" + java.io.File.separator, "")
                .replace("uploads/", "");
        
        User user = photo.getOwner();
        String username;
        if (user.getSurname() != null) {
            username = user.getName() + " " + user.getSurname();
        } else {
            username = user.getName();
        }

        return new PhotoResponseDto(
                photo.getId(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getAmount(),
                cleanedPath,
                photo.getUploadDate(),
                user.getId(),
                username
        );
    }
}
