package spaceflight.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.model.Sex;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PassengerController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PassengerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceImpl passengerService;

    private List<Passenger> passengers;

    @BeforeAll
    void setUp() {

        passengers = Stream.of(
                new Passenger(1, "Dominik", "Janiga", Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11)),
                new Passenger(2, "Ania", "Kowalska", Sex.FEMALE.toString(), "Poland", null, LocalDate.of(1991, 10, 11)),
                new Passenger(3, "Adam", "Kowalski", Sex.MALE.toString(), "Poland", null, LocalDate.of(1992, 11, 11)),
                new Passenger(4, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10))
        ).collect(Collectors.toList());

    }

    @Test
    void shouldReturnSizeOfPassengersList_afterRequestingRightPathToController() throws Exception {

        BDDMockito.given(passengerService.findAll()).willReturn(passengers);

        mockMvc.perform(get("/passengers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)));

        BDDMockito.verify(passengerService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(passengerService);
    }

    @Test
    void shouldReturnFlightsListAsJson_afterRequestingRightPathToController() throws Exception {

        BDDMockito.given(passengerService.findAll()).willReturn(passengers);

        mockMvc.perform(get("/passengers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Dominik")))
                .andExpect(jsonPath("$[1].firstName", is("Ania")))
                .andExpect(jsonPath("$[2].firstName", is("Adam")))
                .andExpect(jsonPath("$[3].firstName", is("Michael")));

        BDDMockito.verify(passengerService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(passengerService);
    }

    @Test
    void shouldReturnSpecificPassenger_AfterRequestingWithPathVariable() throws Exception {

        BDDMockito.given(passengerService.getPassengerById(4)).willReturn(passengers.get(3));

        mockMvc.perform(get("/passenger/{passengerId}", 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(4)))
                .andExpect(jsonPath("$.firstName", is("Michael")))
                .andExpect(jsonPath("$.lastName", is("Jordan")))
                .andExpect(jsonPath("$.sex", is("MALE")))
                .andExpect(jsonPath("$.country", is("USA")))
                .andExpect(jsonPath("$.notes", is("Basketball player")))
                .andExpect(jsonPath("$.birthDate", is("1966-11-10")));

        BDDMockito.verify(passengerService, Mockito.times(1)).getPassengerById(4);
        BDDMockito.verifyNoMoreInteractions(passengerService);

    }
}
