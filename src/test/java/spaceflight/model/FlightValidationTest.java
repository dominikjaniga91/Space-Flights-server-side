package spaceflight.model;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spaceflight.model.Flight;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Flight throws a validation exception when")
public class FlightValidationTest {

    @Autowired
    FlightServiceImpl flightService;

    @Autowired
    PassengerServiceImpl passengerService;

    @Test
    @DisplayName("start and finish date are the same")
    void shouldThrowsValidationException_afterSaveFlightWithInvalidData(){
        Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 3_000_000.0);

        Assertions.assertThrows(ConstraintViolationException.class, () -> flightService.saveFlight(flight));
    }


}
