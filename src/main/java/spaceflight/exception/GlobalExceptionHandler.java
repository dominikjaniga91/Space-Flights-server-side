package spaceflight.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errors = getCustomErrorMessage(HttpStatus.BAD_REQUEST, ex.getMostSpecificCause().toString() , ex.getClass().toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private CustomErrorMessage getCustomErrorMessage(HttpStatus status, String message, String developerMessage) {
        var errors = new CustomErrorMessage();
        errors.setStatus(status.value());
        errors.setDetail(message);
        errors.setTimestamp(LocalDateTime.now());
        errors.setDeveloperMessage(developerMessage);

        return errors;
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<CustomErrorMessage> getPassengerNotFoundExceptionHandler(PassengerNotFoundException ex){

        var errors = getCustomErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getClass().toString());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<CustomErrorMessage> getFlightNotFoundExceptionHandler(FlightNotFoundException ex){

        var errors = getCustomErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getClass().toString());
        return  new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSearchPassengerDataException.class)
    public ResponseEntity<CustomErrorMessage> getInvalidSearchPassengerDataExceptionHandler(InvalidSearchPassengerDataException ex){

        CustomErrorMessage errors = getCustomErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSearchFlightDataException.class)
    public ResponseEntity<CustomErrorMessage> getInvalidSearchFlightDataExceptionHandler(InvalidSearchFlightDataException ex){

        var errors = getCustomErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorMessage> getConstraintViolationExceptionHandler(ConstraintViolationException ex){
        var violations =  ex.getConstraintViolations();
        var message = violations.stream()
                                .map(ConstraintViolation::getMessageTemplate)
                                .findFirst()
                                .orElse("Data error");

        var errors = getCustomErrorMessage(HttpStatus.BAD_REQUEST, message, violations.toString());
               return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomErrorMessage> getExpiredJwtExceptionHandler(ExpiredJwtException ex){

        var errors = getCustomErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
