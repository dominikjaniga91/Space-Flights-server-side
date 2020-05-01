package spaceflight.exception;

import java.time.LocalDate;

public class InvalidSearchPassengerDataException extends RuntimeException {

    public InvalidSearchPassengerDataException(String firstName, String lastName, LocalDate birthDate) {
        super("One of provided data is wrong. First name " + firstName + " last name " + lastName + " birth date " + birthDate);
    }
}
