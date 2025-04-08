package pl.photomarketapp.photomarketapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UserResponseDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("created_at")
    private Date createdAt;
}
