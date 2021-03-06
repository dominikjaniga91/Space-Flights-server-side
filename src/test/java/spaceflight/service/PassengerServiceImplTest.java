package spaceflight.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spaceflight.exception.PassengerNotFoundException;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.model.Sex;
import spaceflight.service.implementation.FlightServiceImpl;
import spaceflight.service.implementation.PassengerServiceImpl;

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
public class PassengerServiceImplTest {

    @Autowired
    FlightServiceImpl flightService;

    @Autowired
    PassengerServiceImpl passengerService;

    @BeforeAll
    void setUpData() {
        Flight flight = new Flight("Jupiter", LocalDate.of(2020, 11, 25), LocalDate.of(2020, 12, 25), 15, 30000.0);
        flightService.saveFlight(flight);

        List<Passenger> passengers = Stream.of(
                new Passenger("Dominik", "Janiga", Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11)),
                new Passenger("Ania", "Kowalska", Sex.FEMALE.toString(), "Poland", null, LocalDate.of(1991, 10, 11)),
                new Passenger("Adam", "Kowalski", Sex.MALE.toString(), "Poland", null, LocalDate.of(1992, 11, 11)),
                new Passenger("Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10))
        ).collect(Collectors.toList());

        passengers.forEach(passenger -> passengerService.savePassenger(passenger));
    }

    @Test
    @Order(1)
    @DisplayName("right amount of passengers after get all ")
    void shouldReturnFourPassengers_afterGetAllPassengersFromDatabase() {

        Assertions.assertEquals(4, passengerService.findAll().size());
    }

    @Test
    @Order(2)
    @DisplayName("one more passenger after save")
    void shouldReturnFivePassengers_afterSavePassengerToDatabase() {

        Passenger passenger = new Passenger("Joanna", "Nowak", Sex.FEMALE.toString(), "Poland", null, LocalDate.of(1995, 11, 11));
        passengerService.savePassenger(passenger);
        Assertions.assertEquals(5, passengerService.findAll().size());
    }

    @Test
    @Order(3)
    @DisplayName("one passenger less after delete ")
    void shouldReturnFourPassengers_afterDeleteOnePassengerFromDatabase() {

        passengerService.deletePassengerById(5);
        Assertions.assertEquals(4, passengerService.findAll().size());
    }

    @Test
    @Order(4)
    @DisplayName("Michael Jordan after get passenger by id")
    void shouldReturnMichaelJordan_afterGetPassengerById() {

        Passenger passenger = passengerService.getPassengerById(4);
        Assertions.assertEquals("Michael", passenger.getFirstName());
        Assertions.assertEquals("Jordan", passenger.getLastName());

    }

    @Test
    @Order(5)
    @DisplayName("one flight after add flight to passenger")
    @Transactional
    void shouldReturnOneFlight_afterAddFlightToPassenger(){

        passengerService.addFlightsToPassenger(1, 1);
        String flightDestination = passengerService.listOfPassengerFlights(1).get(0).getDestination();
        Assertions.assertEquals("Jupiter", flightDestination);

    }

    @Test
    @Order(6)
    @DisplayName("zero flights after delete flight from passenger")
    @Transactional
    void shouldReturnZeroFlights_afterDeleteFLightFromPassenger(){

        passengerService.deleteFlightFromPassenger(1, 1);
        Assertions.assertEquals(0, flightService.listOfPassengers(1).size());

    }

    @Test
    @Order(7)
    @DisplayName("passenger not found exception after request with non existing id")
    @Transactional
    void shouldReturnException_afterRequestWithBadId(){
        PassengerNotFoundException exception=  Assertions.assertThrows(PassengerNotFoundException.class,
                                                 () -> passengerService.getPassengerById(100));
        Assertions.assertEquals(exception.getMessage(), "Passenger ID: 100 doesn't exist");
    }

}
