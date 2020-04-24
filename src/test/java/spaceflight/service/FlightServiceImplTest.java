package spaceflight.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spaceflight.model.Flight;
import spaceflight.repository.FlightRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightServiceImplTest {

    @Autowired
    FlightRepositoryImpl flightRepository;

    @BeforeAll
    void setUpData(){

        List<Flight> flights = Stream.of(new Flight("Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 15000.0),
                new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,6,25), 15, 30000.0),
                new Flight("Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 20000.0),
                new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 30000.0)).collect(Collectors.toList());

        flights.forEach(flight -> flightRepository.save(flight));
    }


    @Test
    @Order(1)
    void shouldReturnFourFlights_afterGetAllFlightsFromDatabase(){
        Assertions.assertEquals(4, flightRepository.findAll().size());
    }

    @Test
    @Order(2)
    void shouldReturnFiveFlights_afterSaveFlightToDatabase(){
        Flight flight = new Flight("Neptune", LocalDate.of(2020,3,25), LocalDate.of(2021,4,25), 15, 3_000_000.0);
        flightRepository.save(flight);
        Assertions.assertEquals(5, flightRepository.findAll().size());
    }

    @Test
    @Order(3)
    void shouldReturnFourFlight_afterDeleteOneFlightFromDatabase(){

        flightRepository.deleteById(5);
        Assertions.assertEquals(4, flightRepository.findAll().size());
    }

    @Test
    @Order(4)
    void shouldReturnFlightToMoon_afterGetFlightById(){

        Optional<Flight> tmpTight =  flightRepository.getFlightById(1);
        Flight flight = tmpTight.orElseThrow();
        Assertions.assertEquals("Moon", flight.getDestination());
    }


}
