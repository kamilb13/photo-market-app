package pl.photomarketapp.photomarketapp.mapper;

import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.model.User;

public class UserMapper {
    public static UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
