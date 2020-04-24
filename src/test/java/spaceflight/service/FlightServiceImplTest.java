package spaceflight.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
public class FlightServiceImplTest {

    @Autowired
    FlightServiceImpl flightService;

    @Autowired
    PassengerServiceImpl passengerService;

    @BeforeAll
    void setUpData(){

        List<Flight> flights = Stream.of(new Flight("Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 15000.0),
                new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,6,25), 15, 30000.0),
                new Flight("Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 20000.0),
                new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 30000.0)).collect(Collectors.toList());

        flights.forEach(flight -> flightService.saveFlight(flight));
    }


    @Test
    @Order(1)
    void shouldReturnFourFlights_afterGetAllFlightsFromDatabase(){
        Assertions.assertEquals(4, flightService.findAll().size());
    }

    @Test
    @Order(2)
    void shouldReturnFiveFlights_afterSaveFlightToDatabase(){
        Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2021,4,25), 15, 3_000_000.0);
        flightService.saveFlight(flight);
        Assertions.assertEquals(5, flightService.findAll().size());
    }

    @Test
    @Order(3)
    void shouldReturnFourFlight_afterDeleteOneFlightFromDatabase(){

        flightService.deleteFlightById(5);
        Assertions.assertEquals(4, flightService.findAll().size());
    }

    @Test
    @Order(4)
    void shouldReturnFlightToMoon_afterGetFlightById(){

        Flight flight =  flightService.getFlightById(1);
        Assertions.assertEquals("Moon", flight.getDestination());
    }

    @Test
    @Order(5)
    @Transactional
    void shouldReturnOnePassenger_afterAddPassengerToFlight(){

        Passenger passenger = new Passenger(1, "Dominik", "Janiga", Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11));
        passengerService.savePassenger(passenger);
        flightService.addPassengersToFlight(1, 1);

        String passengerName = flightService.listOfPassengers(1).get(0).getFirstName();
        Assertions.assertEquals("Dominik", passengerName);

    }



}
