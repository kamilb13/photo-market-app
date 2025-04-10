package pl.photomarketapp.photomarketapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.photomarketapp.photomarketapp.model.Photo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class UserResponseDto {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("photos")
    private List<Photo> photos;

    public UserResponseDto(Long id, String name, String surname, String phoneNumber, String email, LocalDateTime createdAt, List<Photo> photos) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = createdAt;
        this.photos = photos;
    }

    public UserResponseDto(String name, String surname, String phoneNumber, String email, LocalDateTime createdAt) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = createdAt;
    }
}
