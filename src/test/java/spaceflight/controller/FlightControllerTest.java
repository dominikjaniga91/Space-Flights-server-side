package spaceflight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import spaceflight.model.Flight;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;


@ExtendWith(SpringExtension.class)
@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FlightServiceImpl flightService;

    @MockBean
    PassengerServiceImpl passengerService;
    // każda zależność która została wstrzyknięta w kontrolerze musi zostać dodana do testu

    private SimpleDateFormat dateFormat;

    @Test
    void shouldReturnSizeOfFlightsList_afterRequestingRightPathToController() throws Exception {

        Flight flight1 = new Flight(1,"Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 15000.0);
        Flight flight2 = new Flight(2,"Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0);
        Flight flight3 = new Flight(3,"Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 20000.0);
        Flight flight4 = new Flight(4,"Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0);

        BDDMockito.given(flightService.findAll()).willReturn(List.of(flight1, flight2, flight3, flight4));

        mockMvc.perform(get("/flights")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)));

        BDDMockito.verify(flightService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(flightService);
    }

    @Test
    void shouldReturnFlightsListAsJson_afterRequestingRightPathToController() throws Exception {

        Flight flight1 = new Flight(1,"Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 15000.0);
        Flight flight2 = new Flight(2,"Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0);
        Flight flight3 = new Flight(3,"Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 20000.0);
        Flight flight4 = new Flight(4,"Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0);

        BDDMockito.given(flightService.findAll()).willReturn(List.of(flight1, flight2, flight3, flight4));

        mockMvc.perform(get("/flights")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destination", Matchers.is("Moon")))
                .andExpect(jsonPath("$[1].destination", Matchers.is("Jupiter")))
                .andExpect(jsonPath("$[2].destination", Matchers.is("Mars")))
                .andExpect(jsonPath("$[3].destination", Matchers.is("Jupiter")));

        BDDMockito.verify(flightService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(flightService);
    }

    @Test
    void shouldReturnSpecificFlight_AfterRequestingWithPathVariable() throws Exception {

        Flight flight = new Flight(4,"Jupiter", LocalDate.of(2020,4,25), LocalDate.of(2020,5,25), 15, 80000.0);
        BDDMockito.given(flightService.getFlightById(anyInt())).willReturn(flight);

       mockMvc.perform(get("/flight/{id}", 1)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", Matchers.is(4)))
               .andExpect(jsonPath("$.destination", Matchers.is("Jupiter")))
               .andExpect(jsonPath("$.startDate", Matchers.is("2020-04-25")))
               .andExpect(jsonPath("$.finishDate", Matchers.is("2020-05-25")))
               .andExpect(jsonPath("$.ticketPrice", Matchers.is(80000.0)));

        BDDMockito.verify(flightService, Mockito.times(1)).getFlightById(anyInt());
        BDDMockito.verifyNoMoreInteractions(flightService);

    }

    @Test
    void shouldReturnFlight_afterRequestForSaveFlight() throws Exception {
        Flight flight = new Flight(4,"Jupiter", LocalDate.of(2020,4,25), LocalDate.of(2020,5,25), 15, 80000.0);
        BDDMockito.given(flightService.saveFlight(any(Flight.class))).willReturn(flight);

        mockMvc.perform(post("/flight")
                .content(new ObjectMapper().writeValueAsString(flight))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(4)))
                .andExpect(jsonPath("$.destination", Matchers.is("Jupiter")))
                .andExpect(jsonPath("$.startDate", Matchers.is("2020-04-25")))
                .andExpect(jsonPath("$.finishDate", Matchers.is("2020-05-25")))
                .andExpect(jsonPath("$.ticketPrice", Matchers.is(80000.0)));

        BDDMockito.verify(flightService, Mockito.times(1)).saveFlight(any(Flight.class));
        BDDMockito.verifyNoMoreInteractions(flightService);
    }


    @Test
    void shouldDeleteFlight_afterRequestingRightPath() throws Exception {

        BDDMockito.doNothing().when(flightService).deleteFlightById(1);

        mockMvc.perform(delete("/flight/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());

        BDDMockito.verify(flightService).deleteFlightById(1);
    }

    @Test
    void shouldUpdateFlight_afterRequestingRightPath() throws Exception {
        Flight flight = new Flight(1,"Neptune", LocalDate.of(2020,4,25), LocalDate.of(2021,5,25), 15, 1_000_000.00);
        BDDMockito.given(flightService.updateFlight(any(Flight.class))).willReturn(flight);

        mockMvc.perform(put("/flight")
                .content(new ObjectMapper().writeValueAsString(flight))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        BDDMockito.verify(flightService, Mockito.times(1)).updateFlight(any(Flight.class));
        BDDMockito.verifyNoMoreInteractions(flightService);

    }

}
