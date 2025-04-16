package pl.photomarketapp.photomarketapp.service;

import org.springframework.stereotype.Service;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.exception.AddUserException;
import pl.photomarketapp.photomarketapp.mapper.UserMapper;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto addUser(UserRequestDto userRequestDto) throws AddUserException {
        User user = new User(
                userRequestDto.getName(),
                userRequestDto.getSurname(),
                userRequestDto.getPhoneNumber(),
                userRequestDto.getEmail()
        );
        userRepository.save(user);
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getPhotos()
        );
    }

    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
