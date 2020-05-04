package spaceflight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spaceflight.model.Passenger;
import spaceflight.model.Sex;
import spaceflight.service.PassengerServiceImpl;
import java.time.LocalDate;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Api user with ")
@ActiveProfiles("test")
public class SecurityTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceImpl passengerService;


    @Nested
    @DisplayName("admin role")
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    class Admin {

        String token;

        @BeforeEach
        void setUp() {
        token = Jwts.builder().setSubject("admin")
                    .claim("role", "ADMIN")
                    .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                    .signWith(SignatureAlgorithm.HS512, "SecretKey")
                    .compact();
        }

        @Test
        @DisplayName("has access to GET method")
        void shouldAuthenticatedUser_afterGetRequest() throws Exception {
            mockMvc.perform(get("/api/passengers")
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("has access to POST method")
        void shouldAuthenticatedUser_afterPostRequest() throws Exception {
            Passenger passenger = new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
            BDDMockito.given(passengerService.savePassenger(any(Passenger.class))).willReturn(passenger);

            mockMvc.perform(post("/api/passenger")
                    .content(new ObjectMapper().writeValueAsString(passenger))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("has access to DELETE method")
        void shouldAuthenticatedUser_afterDeleteRequest() throws Exception {
            BDDMockito.doNothing().when(passengerService).deletePassengerById(1);

            mockMvc.perform(delete("/api/passenger/{passengerId}", 1)
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("has access to PUT method")
        void shouldAuthenticatedUser_afterPutRequest() throws Exception {
            Passenger passenger = new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
            BDDMockito.given(passengerService.savePassenger(any(Passenger.class))).willReturn(passenger);

            mockMvc.perform(put("/api/passenger")
                    .content(new ObjectMapper().writeValueAsString(passenger))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("manager or employee role")
    @WithMockUser(username = "darek", password = "darek123", roles = {"EMPLOYEE"})
    class Manager {

        String token;

        @BeforeEach
        void setUp() {
            token = Jwts.builder().setSubject("darek")
                    .claim("role", "EMPLOYEE")
                    .setExpiration(new Date(System.currentTimeMillis() + 86_400_000))
                    .signWith(SignatureAlgorithm.HS512, "SecretKey")
                    .compact();
        }

        @Test
        @DisplayName("has access to GET method")
        void shouldAuthenticatedUser_afterGetRequest() throws Exception {
            mockMvc.perform(get("/api/passengers")
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("has access to POST method")
        void shouldAuthenticatedUser_afterPostRequest() throws Exception {
            Passenger passenger = new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
            BDDMockito.given(passengerService.savePassenger(any(Passenger.class))).willReturn(passenger);

            mockMvc.perform(post("/api/passenger")
                    .content(new ObjectMapper().writeValueAsString(passenger))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("has NOT access to DELETE method")
        void shouldAuthenticatedUser_afterDeleteRequest() throws Exception {
            BDDMockito.doNothing().when(passengerService).deletePassengerById(1);

            mockMvc.perform(delete("/api/passenger/{passengerId}", 1)
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("has access to PUT method")
        void shouldAuthenticatedUser_afterPutRequest() throws Exception {
            Passenger passenger = new Passenger(5, "Michael", "Jordan", Sex.MALE.toString(), "USA", "Basketball player", LocalDate.of(1966, 11, 10));
            BDDMockito.given(passengerService.savePassenger(any(Passenger.class))).willReturn(passenger);

            mockMvc.perform(put("/api/passenger")
                    .content(new ObjectMapper().writeValueAsString(passenger))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .header("Access-Control-Expose-Headers", "Authorization"))
                    .andExpect(status().isOk());
        }
    }
}
