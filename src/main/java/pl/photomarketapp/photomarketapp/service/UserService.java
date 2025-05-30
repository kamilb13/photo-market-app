package pl.photomarketapp.photomarketapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
//import pl.photomarketapp.photomarketapp.exception.AddUserException;
import pl.photomarketapp.photomarketapp.mapper.UserMapper;
import pl.photomarketapp.photomarketapp.model.Photo;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto addUser(UserRequestDto userRequestDto) { //throws AddUserException
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
                user.getUploadedPhotos()
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String getUsernameByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getName() + " " + user.getSurname();
    }

    public List<Photo> getPurchasedPhotos() {
        return Collections.emptyList();
    }

    public List<Photo> getUploadedPhotos() {
        return Collections.emptyList();
    }

    public void buyPhoto(Photo photo) {

    }


}
