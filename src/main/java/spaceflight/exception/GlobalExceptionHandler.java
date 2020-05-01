package spaceflight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<String> getPassengerNotFoundExceptionHandler(PassengerNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<String> getFlightNotFoundExceptionHandler(FlightNotFoundException ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSearchPassengerDataException.class)
    public ResponseEntity<String> getInvalidSearchPassengerDataExceptionHandler(InvalidSearchPassengerDataException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSearchFlightDataException.class)
    public ResponseEntity<String> getInvalidSearchFlightDataExceptionHandler(InvalidSearchFlightDataException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> getConstraintViolationExceptionHandler(ConstraintViolationException ex){
        var violations =  ex.getConstraintViolations();
        var message = violations.stream()
                                .map(ConstraintViolation::getMessageTemplate)
                                .findFirst()
                                .orElse("Data error");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> getUserNotExistExceptionHandler(InternalAuthenticationServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
