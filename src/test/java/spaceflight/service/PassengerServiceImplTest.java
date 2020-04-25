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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PassengerServiceImplTest {

    @Autowired
    FlightServiceImpl flightService;

    @Autowired
    PassengerServiceImpl passengerService;

    @BeforeAll
    void setUpData(){
        Flight flight = new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,6,25), 15, 30000.0);
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
    void shouldReturnFourPassengers_afterGetAllPassengersFromDatabase(){

        Assertions.assertEquals(4, passengerService.findAll().size());
    }

    @Test
    @Order(2)
    void shouldReturnFivePassengers_afterSavePassengerToDatabase(){

        Passenger passenger = new Passenger("Joanna", "Nowak", Sex.FEMALE.toString(), "Poland", null, LocalDate.of(1995, 11, 11));
        passengerService.savePassenger(passenger);
        Assertions.assertEquals(5, passengerService.findAll().size());
    }

    @Test
    @Order(3)
    void shouldReturnFourPassengers_afterDeleteOnePassengerFromDatabase(){

        passengerService.deletePassengerById(5);
        Assertions.assertEquals(4, passengerService.findAll().size());
    }
}
