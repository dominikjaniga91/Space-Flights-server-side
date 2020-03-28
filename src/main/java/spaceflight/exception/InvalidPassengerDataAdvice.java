package spaceflight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidPassengerDataAdvice {

    @ExceptionHandler(InvalidPassengerDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getInvalidPassengerDataExceptionHandler(InvalidPassengerDataException ex){

        return ex.getMessage();
    }

}
