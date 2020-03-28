package spaceflight.exception;

import java.time.LocalDate;

public class InvalidPassengerDataException extends RuntimeException {

    public InvalidPassengerDataException(String firstName, String lastName, LocalDate birthDate) {
        super("One of provided data is wrong. First name " + firstName + " last name " + lastName + " birth date " + birthDate);
    }
}
