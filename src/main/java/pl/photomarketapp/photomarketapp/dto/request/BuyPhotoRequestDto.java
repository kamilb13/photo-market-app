package pl.photomarketapp.photomarketapp.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuyPhotoRequestDto {
    private Long userId;
    private Long photoId;
}
