package spaceflight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import spaceflight.authentication.UserDetailServiceImpl;
import spaceflight.model.Flight;
import spaceflight.service.implementation.FlightServiceImpl;
import org.hamcrest.Matchers;
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
import static org.hamcrest.Matchers.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FlightController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
@DisplayName("Request to flight controller using http method")
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightServiceImpl flightService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    private List<Flight> flights;
    private String token;

    @BeforeAll
    void setUp() {

        flights = Stream.of(new Flight(1,"Moon", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 15000.0),
                            new Flight(2,"Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 30000.0),
                            new Flight(3,"Mars", LocalDate.of(2020,3,25), LocalDate.of(2020,3,25), 15, 20000.0),
                            new Flight(4,"Jupiter", LocalDate.of(2020,3,25), LocalDate.of(2020,4,25), 15, 30000.0)).collect(Collectors.toList());
        token = Jwts.builder().setSubject("admin")
                .claim("role", "ADMIN")
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                .signWith(SignatureAlgorithm.HS512, "SecretKey")
                .compact();

    }

    @Test
    @DisplayName("GET should return status ok and right amount of flights")
    void shouldReturnSizeOfFlightsList_afterRequestingRightPathToController() throws Exception {

        BDDMockito.given(flightService.findAll()).willReturn(flights);

        mockMvc.perform(get("/api/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)));

        BDDMockito.verify(flightService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(flightService);
    }

    @Test
    @DisplayName("GET should return status ok and JSON objects")
    void shouldReturnFlightsListAsJson_afterRequestingRightPathToController() throws Exception {

        BDDMockito.given(flightService.findAll()).willReturn(flights);

        mockMvc.perform(get("/api/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destination", is("Moon")))
                .andExpect(jsonPath("$[1].destination", is("Jupiter")))
                .andExpect(jsonPath("$[2].destination", is("Mars")))
                .andExpect(jsonPath("$[3].destination", is("Jupiter")));

        BDDMockito.verify(flightService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(flightService);
    }

    @Test
    @DisplayName("GET with id parameter should return status ok, JSON object and verify method")
    void shouldReturnSpecificFlight_AfterRequestingWithPathVariable() throws Exception {

        BDDMockito.given(flightService.getFlightById(anyInt())).willReturn(flights.get(3));

        mockMvc.perform(get("/api/flight/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(4)))
                .andExpect(jsonPath("$.destination", is("Jupiter")))
                .andExpect(jsonPath("$.startDate", is("2020-03-25")))
                .andExpect(jsonPath("$.finishDate", is("2020-04-25")))
                .andExpect(jsonPath("$.numberOfSeats", is(15)))
                .andExpect(jsonPath("$.ticketPrice", is(30000.0)));

        BDDMockito.verify(flightService, Mockito.times(1)).getFlightById(anyInt());
        BDDMockito.verifyNoMoreInteractions(flightService);

    }


    @Test
    @DisplayName("POST should return status created, save JSON object and verify method")
    void shouldReturnFlight_afterRequestForSaveFlight() throws Exception {
        Flight flight = new Flight(5,"Jupiter", LocalDate.of(2020,4,25), LocalDate.of(2020,5,25), 15, 80000.0);
        BDDMockito.given(flightService.saveFlight(any(Flight.class))).willReturn(flight);

        mockMvc.perform(post("/api/flight")
                .content(new ObjectMapper().writeValueAsString(flight))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.destination", is("Jupiter")))
                .andExpect(jsonPath("$.startDate", is("2020-04-25")))
                .andExpect(jsonPath("$.finishDate", is("2020-05-25")))
                .andExpect(jsonPath("$.ticketPrice", is(80000.0)));


        BDDMockito.verify(flightService, Mockito.times(1)).saveFlight(any(Flight.class));
        BDDMockito.verifyNoMoreInteractions(flightService);
    }


    @Test
    @DisplayName("DELETE with id parameter should return status ok and verify method")
    void shouldDeleteFlight_afterRequestingRightPath() throws Exception {

        BDDMockito.doNothing().when(flightService).deleteFlightById(1);

        mockMvc.perform(delete("/api/flight/{id}", 1)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isOk());

        BDDMockito.verify(flightService).deleteFlightById(1);
    }

    @Test
    @DisplayName("PUT should return status ok and verify method")
    void shouldUpdateFlight_afterRequestingRightPath() throws Exception {
        Flight flight = new Flight(1,"Neptune", LocalDate.of(2020,4,25), LocalDate.of(2021,5,25), 15, 1_000_000.00);
        BDDMockito.given(flightService.updateFlight(any(Flight.class))).willReturn(flight);

        mockMvc.perform(put("/api/flight")
                .content(new ObjectMapper().writeValueAsString(flight))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk());

        BDDMockito.verify(flightService, Mockito.times(1)).updateFlight(any(Flight.class));
        BDDMockito.verifyNoMoreInteractions(flightService);

    }

    @Test
    @DisplayName("POST should return bad request after send invalid data")
    void shouldReturnBadRequest_afterSendRequestWithBadData() throws Exception {

        String flight = "{\"id\": \"dfwwd\",\n" +
                " \"destination\": 9234,\n" +
                " \"startDate\": \"5324vvv4636\",\n" +
                " \"finishDate\": 60t4tgwt0,\n" +
                " \"numberOfSeats\": \"ffffff,\n" +
                " \"ticketPrice\": \"ffffff\"}";

        mockMvc.perform(post("/api/flight")
                .content(flight)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT with flight and passenger id should return status ok")
    void shouldReturnStatusOk_afterRequestPutPassengerInFlight() throws Exception {

        int[] id = { 1 };
        Mockito.doNothing().when(flightService).addPassengersToFlight(1, id);

        mockMvc.perform(put("/api/flight/passengers/{flightId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(id))
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE with flight and passenger id should return status ok")
    void shouldReturnStatusOk_afterRequestDeletePassengerFromFlight() throws Exception {

        BDDMockito.doNothing().when(flightService).deletePassengerFromFlight(1, 1);
        mockMvc.perform(delete("/api/flight/passengers/{flightId}/{passengerId}", 1, 1)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isOk());

        BDDMockito.verify(flightService).deletePassengerFromFlight(1,1);
    }


}
