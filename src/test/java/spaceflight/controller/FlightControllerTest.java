package spaceflight.controller;

import org.mockito.Mockito;
import spaceflight.model.Flight;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @BeforeEach
    void setUp() {

        Flight flight1 = new Flight("Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 15000.0);
        Flight flight2 = new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0);
        Flight flight3 = new Flight("Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 20000.0);
        Flight flight4 = new Flight("Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0);

        BDDMockito.given(flightService.findAll()).willReturn(List.of(flight1, flight2, flight3, flight4));
    }

    @Test
    void shouldReturnSizeOfFlightsList_afterRequestingRightPathToController() throws Exception {
        mockMvc.perform(get("/flights")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)));

        BDDMockito.verify(flightService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(flightService);

    }

    @Test
    void shouldReturnFlightsListAsJson_afterRequestingRightPathToController() throws Exception {
        mockMvc.perform(get("/flights")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destination", Matchers.is("Moon")))
                .andExpect(jsonPath("$[1].ticketPrice", Matchers.is(30000.0)))
                .andExpect(jsonPath("$[2].startDate", Matchers.is("2020-03-25")))
                .andExpect(jsonPath("$[3].destination", Matchers.is("Jupiter")));

        BDDMockito.verify(flightService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(flightService);
    }

}
