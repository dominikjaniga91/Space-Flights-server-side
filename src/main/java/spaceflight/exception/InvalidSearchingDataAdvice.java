package spaceflight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidSearchingDataAdvice {

    @ExceptionHandler(InvalidSearchingDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getInvalidSearchingDataExceptionHandler(InvalidSearchingDataException ex){

        return ex.getMessage();
    }

}
