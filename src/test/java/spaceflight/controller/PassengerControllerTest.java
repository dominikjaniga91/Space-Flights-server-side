package spaceflight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    void shouldReturnPassenger_afterRequestForSavePassenger() throws Exception {
        Passenger passenger =  new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
        BDDMockito.given(passengerService.savePassenger(any(Passenger.class))).willReturn(passenger);

        mockMvc.perform(post("/passenger")
                .content(new ObjectMapper().writeValueAsString(passenger))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(5)))
                .andExpect(jsonPath("$.firstName", is("Michael")))
                .andExpect(jsonPath("$.lastName", is("Jordan")))
                .andExpect(jsonPath("$.sex", is("MALE")))
                .andExpect(jsonPath("$.country", is("USA")))
                .andExpect(jsonPath("$.notes", is("Basketball player")))
                .andExpect(jsonPath("$.birthDate", is("1966-11-10")));


        BDDMockito.verify(passengerService, Mockito.times(1)).savePassenger(any(Passenger.class));
        BDDMockito.verifyNoMoreInteractions(passengerService);
    }

    @Test
    void shouldDeletePassenger_afterRequestingRightPath() throws Exception {

        BDDMockito.doNothing().when(passengerService).deletePassengerById(1);

        mockMvc.perform(delete("/passenger/{passengerId}", 1))
                .andDo(print())
                .andExpect(status().isOk());

        BDDMockito.verify(passengerService).deletePassengerById(1);
    }

    @Test
    void shouldUpdatePassenger_afterRequestingRightPath() throws Exception {
        Passenger passenger =  new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
        BDDMockito.given(passengerService.updatePassenger(any(Passenger.class))).willReturn(passenger);

        mockMvc.perform(put("/passenger")
                .content(new ObjectMapper().writeValueAsString(passenger))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        BDDMockito.verify(passengerService, Mockito.times(1)).updatePassenger(any(Passenger.class));
        BDDMockito.verifyNoMoreInteractions(passengerService);

    }

    @Test
    void shouldReturnBadRequest_afterSendRequestWithBadData() throws Exception {

        String flight = "{\"id\": \"dfwwd\",\n" +
                " \"firstName\": 9234,\n" +
                " \"lastName\": \"5324vvv4636\",\n" +
                " \"sex\": 60t4tgwt0,\n" +
                " \"country\": \"ffffff,\n" +
                " \"notes\": \"ffffff,\n" +
                " \"birthDate\": \"ffffff\"}";

        mockMvc.perform(post("/passenger")
                .content(flight)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
