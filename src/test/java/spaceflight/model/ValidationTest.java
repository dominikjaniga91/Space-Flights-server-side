package spaceflight.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Throws an validation exception when")
public class ValidationTest {

    @Autowired
    FlightServiceImpl flightService;

    @Autowired
    PassengerServiceImpl passengerService;

    @Nested
    @DisplayName("flight's")
    class FlightValidation{

        @Test
        @DisplayName("start and finish date are the same")
        void shouldThrowsValidationException_whileSavingFlightTheSameData(){
            Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 3_000_000.0);
            Assertions.assertThrows(ConstraintViolationException.class, () -> flightService.saveFlight(flight));
        }

        @Test
        @DisplayName("finish date are earlier than start date")
        void shouldThrowsValidationException_whileSavingFlightInvalidFinishData(){
            Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2019,3,25), 15, 3_000_000.0);
            Assertions.assertThrows(ConstraintViolationException.class, () -> flightService.saveFlight(flight));
        }

        @Test
        @DisplayName("ticket price is negative")
        void shouldThrowsValidationException_whileSavingFlightWithNegativePrice(){
            Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2019,4,25), 15, -100);
            Assertions.assertThrows(ConstraintViolationException.class, () -> flightService.saveFlight(flight));

        }

        @Test
        @DisplayName("number of seats is negative")
        void shouldThrowsValidationException_whileSavingFlightWithNegativeSeatsNumber(){
            Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2019,4,25), -5, 3_000_000.0);
            Assertions.assertThrows(ConstraintViolationException.class, () -> flightService.saveFlight(flight));

        }
    }



    @ParameterizedTest(name = "{1}")
    @MethodSource("passengerProvider")
    @DisplayName("passenger's data are null")
    void shouldThrowsValidationException_whileSavingFlightTheSameData(Passenger passenger, String name){

        Assertions.assertThrows(ConstraintViolationException.class, () -> passengerService.savePassenger(passenger));
    }

    static Stream<Arguments> passengerProvider() {

        Passenger firstNameNull = new Passenger(null, "Janiga", Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11));
        Passenger lastNameNull = new Passenger("Dominik", null, Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11));
        Passenger sexNull = new Passenger("Dominik", "Janiga", null, "Poland", null, LocalDate.of(1990, 11, 11));
        Passenger countryNull = new Passenger("Dominik", "Janiga", Sex.MALE.toString(), null, null, LocalDate.of(1990, 11, 11));
        Passenger birthDateNull = new Passenger("Dominik", "Janiga", Sex.MALE.toString(), "Poland", null, null);

        return Stream.of(
                Arguments.arguments(firstNameNull, "first name") ,
                Arguments.arguments(lastNameNull, "last name") ,
                Arguments.arguments(sexNull, "sex") ,
                Arguments.arguments(countryNull, "country"),
                Arguments.arguments(birthDateNull, "birth date"));
    }




}
