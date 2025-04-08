package pl.photomarketapp.photomarketapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.photomarketapp.photomarketapp.model.User;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import java.util.List;

@Tag(name = "Kontroler klienta", description = "")
@RestController
public class ClientController {
    private final UserRepository userRepository;

    @Autowired
    public ClientController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/add-client")
    public User addClient(@RequestBody User client) {
        return userRepository.save(client);
    }

    @GetMapping("/get-all-clients")
    public List<User> getAllClients() {
        return userRepository.findAll();
    }
}
