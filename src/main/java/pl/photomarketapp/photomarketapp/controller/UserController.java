package pl.photomarketapp.photomarketapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.photomarketapp.photomarketapp.dto.request.UserRequestDto;
import pl.photomarketapp.photomarketapp.dto.response.UserResponseDto;
import pl.photomarketapp.photomarketapp.repository.UserRepository;
import pl.photomarketapp.photomarketapp.service.UserService;

import java.util.List;

@Tag(name = "Kontroler usera", description = "")
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/add-user")
    public UserResponseDto addUser(@RequestBody UserRequestDto user) {
        return userService.addUser(user);
    }

    @GetMapping("/get-all-users")
    public List<UserResponseDto> getAllUsers() {
        return userService.getUsers();
    }
}
