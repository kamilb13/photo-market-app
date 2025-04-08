package pl.photomarketapp.photomarketapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PhotoUploadException.class)
    public ResponseEntity<ErrorResponse> handlePhotoUploadException(PhotoUploadException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Photo upload failed: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
