package pl.photomarketapp.photomarketapp.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.exception.AddUserException;
import pl.photomarketapp.photomarketapp.mapper.UserMapper;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

//    public boolean existsByUsername(String username) {
//        return userRepository.existsByUsername(username);
//    }

    public void registerUser(RegistrationRequest registrationRequest) {
//        if (existsByUsername(registrationRequest.getPhoneNumber())) {
//            throw new RuntimeException("Username already exists");
//        }
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setSurname(registrationRequest.getSurname());
        user.setEmail(registrationRequest.getEmail());
        user.setPhoneNumber(registrationRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
