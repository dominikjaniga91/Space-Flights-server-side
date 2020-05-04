package spaceflight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spaceflight.authentication.UserDetailServiceImpl;
import spaceflight.model.Passenger;
import spaceflight.model.Sex;
import spaceflight.service.PassengerServiceImpl;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PassengerController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "Dominik", roles = {"ADMIN"})
@DisplayName("Request to passenger controller using http method")
public class PassengerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceImpl passengerService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    private List<Passenger> passengers;
    private String token;

    @BeforeAll
    void setUp() {

        passengers = Stream.of(
                new Passenger(1, "Dominik", "Janiga", Sex.MALE.toString(), "Poland", null, LocalDate.of(1990, 11, 11)),
                new Passenger(2, "Ania", "Kowalska", Sex.FEMALE.toString(), "Poland", null, LocalDate.of(1991, 10, 11)),
                new Passenger(3, "Adam", "Kowalski", Sex.MALE.toString(), "Poland", null, LocalDate.of(1992, 11, 11)),
                new Passenger(4, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10))
        ).collect(Collectors.toList());

        token = Jwts.builder().setSubject("admin")
                .claim("role", "ADMIN")
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                .signWith(SignatureAlgorithm.HS512, "SecretKey")
                .compact();
    }

    @Test
    @DisplayName("get should return status ok and right amount of passengers")
    void shouldReturnSizeOfPassengersList_afterRequestingRightPathToController() throws Exception {

        BDDMockito.given(passengerService.findAll()).willReturn(passengers);

        mockMvc.perform(get("/api/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)));

        BDDMockito.verify(passengerService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(passengerService);
    }

    @Test
    @DisplayName("get should return status ok and JSON objects and verify method")
    void shouldReturnFlightsListAsJson_afterRequestingRightPathToController() throws Exception {

        BDDMockito.given(passengerService.findAll()).willReturn(passengers);

        mockMvc.perform(get("/api/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Dominik")))
                .andExpect(jsonPath("$[1].firstName", is("Ania")))
                .andExpect(jsonPath("$[2].firstName", is("Adam")))
                .andExpect(jsonPath("$[3].firstName", is("Michael")));

        BDDMockito.verify(passengerService, Mockito.times(1)).findAll();
        BDDMockito.verifyNoMoreInteractions(passengerService);
    }

    @Test
    @DisplayName("get with id parameter should return status ok, JSON object and verify method")
    void shouldReturnSpecificPassenger_AfterRequestingWithPathVariable() throws Exception {

        BDDMockito.given(passengerService.getPassengerById(4)).willReturn(passengers.get(3));

        mockMvc.perform(get("/api/passenger/{passengerId}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
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
    @DisplayName("post should return status created, save JSON object and verify method")
    void shouldReturnPassenger_afterRequestForSavePassenger() throws Exception {
        Passenger passenger =  new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
        BDDMockito.given(passengerService.savePassenger(any(Passenger.class))).willReturn(passenger);

        mockMvc.perform(post("/api/passenger")
                .content(new ObjectMapper().writeValueAsString(passenger))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
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
    @DisplayName("delete with id parameter should return status ok and verify method")
    void shouldDeletePassenger_afterRequestingRightPath() throws Exception {

        BDDMockito.doNothing().when(passengerService).deletePassengerById(1);

        mockMvc.perform(delete("/api/passenger/{passengerId}", 1)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isOk());

        BDDMockito.verify(passengerService).deletePassengerById(1);
    }

    @Test
    @DisplayName("put should return status ok and verify method")
    void shouldUpdatePassenger_afterRequestingRightPath() throws Exception {
        Passenger passenger =  new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
        BDDMockito.given(passengerService.updatePassenger(any(Passenger.class))).willReturn(passenger);

        mockMvc.perform(put("/api/passenger")
                .content(new ObjectMapper().writeValueAsString(passenger))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(status().isOk());

        BDDMockito.verify(passengerService, Mockito.times(1)).updatePassenger(any(Passenger.class));
        BDDMockito.verifyNoMoreInteractions(passengerService);

    }

    @Test
    @DisplayName("post should return bad request after send invalid data")
    void shouldReturnBadRequest_afterSendRequestWithBadData() throws Exception {

        String flight = "{\"id\": \"dfwwd\",\n" +
                " \"firstName\": 9234,\n" +
                " \"lastName\": \"5324vvv4636\",\n" +
                " \"sex\": 60t4tgwt0,\n" +
                " \"country\": \"ffffff,\n" +
                " \"notes\": \"ffffff,\n" +
                " \"birthDate\": \"ffffff\"}";

        mockMvc.perform(post("/api/passenger")
                .content(flight)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("put with flight and passenger id should return status ok")
    void shouldReturnStatusOk_afterRequestPutFlightForPassenger() throws Exception {

        int[] flightId = { 1 };
        Mockito.doNothing().when(passengerService).addFlightsToPassenger(1, flightId);

        mockMvc.perform(put("/api/passenger/flights/{passengerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(flightId))
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("delete with flight and passenger id should return status ok")
    void shouldReturnStatusOk_afterRequestDeletePassengerFromFlight() throws Exception {

        BDDMockito.doNothing().when(passengerService).deleteFlightFromPassenger(1, 1);
        mockMvc.perform(delete("/api/passenger/flights/{passengerId}/{flightId}", 1, 1)
                .header("Authorization", token)
                .header("Access-Control-Expose-Headers", "Authorization"))
                .andDo(print())
                .andExpect(status().isOk());
        BDDMockito.verify(passengerService).deleteFlightFromPassenger(1,1);
    }
}
