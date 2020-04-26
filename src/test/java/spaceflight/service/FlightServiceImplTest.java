package spaceflight.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spaceflight.exception.FlightNotFoundException;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.model.Sex;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Integration test with database should return")
public class FlightServiceImplTest {

    @Autowired
    FlightServiceImpl flightService;

    @Autowired
    PassengerServiceImpl passengerService;

    @BeforeAll
    void setUpData(){

        Passenger passenger = new Passenger("Dominik", "Janiga", Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11));
        passengerService.savePassenger(passenger);

        List<Flight> flights = Stream.of(new Flight("Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 15000.0),
                new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,6,25), 15, 30000.0),
                new Flight("Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 20000.0),
                new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 30000.0)).collect(Collectors.toList());

        flights.forEach(flight -> flightService.saveFlight(flight));
    }


    @Test
    @Order(1)
    @DisplayName("right amount of flights after get all ")
    void shouldReturnFourFlights_afterGetAllFlightsFromDatabase(){

        Assertions.assertEquals(4, flightService.findAll().size());
    }

    @Test
    @Order(2)
    @DisplayName("one more flight after save")
    void shouldReturnFiveFlights_afterSaveFlightToDatabase(){

        Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2021,4,25), 15, 3_000_000.0);
        flightService.saveFlight(flight);
        Assertions.assertEquals(5, flightService.findAll().size());
    }

    @Test
    @Order(3)
    @DisplayName("one flight less after delete ")
    void shouldReturnFourFlight_afterDeleteOneFlightFromDatabase(){

        flightService.deleteFlightById(5);
        Assertions.assertEquals(4, flightService.findAll().size());
    }

    @Test
    @Order(4)
    @DisplayName("flight to Moon after get flight by id")
    void shouldReturnFlightToMoon_afterGetFlightById(){

        Flight flight =  flightService.getFlightById(1);
        Assertions.assertEquals("Moon", flight.getDestination());
    }

    @Test
    @Order(5)
    @DisplayName("one passenger after add passenger to flight")
    @Transactional
    void shouldReturnOnePassenger_afterAddPassengerToFlight(){

        flightService.addPassengersToFlight(1, 1);
        String passengerName = flightService.listOfPassengers(1).get(0).getFirstName();
        Assertions.assertEquals("Dominik", passengerName);

    }

    @Test
    @Order(6)
    @DisplayName("zero passengers after delete passenger from flight")
    @Transactional
    void shouldReturnZeroPassengers_afterDeletePassengerToFlight(){

        flightService.deletePassengerFromFlight(1, 1);
        Assertions.assertEquals(0, flightService.listOfPassengers(1).size());

    }

    @Test
    @Order(7)
    @DisplayName("flight not found exception after request with non existing id")
    @Transactional
    void shouldReturnException_afterRequestWithBadId(){
        FlightNotFoundException exception = Assertions.assertThrows(FlightNotFoundException.class,
                                                                    () -> flightService.getFlightById(100));

        Assertions.assertEquals(exception.getMessage(), "Flight ID: 100 doesn't exist");
    }



}
