package pl.photomarketapp.photomarketapp.IntegrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.photomarketapp.photomarketapp.dto.request.EmailRequest;
import pl.photomarketapp.photomarketapp.dto.request.LoginRequest;
import pl.photomarketapp.photomarketapp.dto.request.RegistrationRequest;
import pl.photomarketapp.photomarketapp.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("me@example.com");
        request.setPassword("Secure123!");
        request.setName("Rafał");
        request.setSurname("Nowak");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCurrentUserData() throws Exception {
        // Logowanie i pobranie tokena
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("me@example.com");
        loginRequest.setPassword("Secure123!");

        var loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode loginJson = objectMapper.readTree(loginResponse);
        String jwtToken = loginJson.get("jwtToken").asText();

        // Przygotowanie EmailRequest jako body
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("me@example.com");

        // Zapytanie do /users/username
        mockMvc.perform(post("/users/username")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Rafał Nowak"));
    }
}