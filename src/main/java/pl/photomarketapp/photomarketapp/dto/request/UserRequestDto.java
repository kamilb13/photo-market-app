package pl.photomarketapp.photomarketapp.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
}
