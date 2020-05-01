package spaceflight.exception;

import java.time.LocalDate;

public class InvalidSearchFlightDataException extends RuntimeException {

    public InvalidSearchFlightDataException(LocalDate startDate, LocalDate finishDate, String destination) {
        super("One of provided data is wrong. Start date " + startDate + " finish date " + finishDate + " destination " + destination);
    }
}
