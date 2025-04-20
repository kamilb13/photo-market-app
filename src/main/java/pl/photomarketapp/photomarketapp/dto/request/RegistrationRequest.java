package pl.photomarketapp.photomarketapp.dto.request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String name;
    private String surname;
    private String password;
    private String email;
    private String phoneNumber;
}
