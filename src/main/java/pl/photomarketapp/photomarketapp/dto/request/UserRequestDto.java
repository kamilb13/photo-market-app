package pl.photomarketapp.photomarketapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDto {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
}
