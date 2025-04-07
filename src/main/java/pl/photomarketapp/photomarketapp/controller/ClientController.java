package pl.photomarketapp.photomarketapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.photomarketapp.photomarketapp.model.Client;
import pl.photomarketapp.photomarketapp.repository.ClientRepository;

import java.util.List;

@Tag(name = "Kontroler klienta", description = "")
@RestController
public class ClientController {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping("/add-client")
    public Client addClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @GetMapping("/get-all-clients")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
