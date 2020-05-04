package spaceflight.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="first_name")
    @NotBlank(message = "Please provide first name")
    private String firstName;

    @Column(name="last_name")
    @NotBlank(message = "Please provide last name")
    private String lastName;

    @NotNull
    private String sex;

    @NotBlank(message = "Please provide country")
    private String country;

    private String notes;

    @Column(name="birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name="passengers_flights",
            joinColumns = @JoinColumn(name="passenger_id", updatable = false),
            inverseJoinColumns = @JoinColumn(name="flight_id", updatable = false))
    private List<Flight> listOfFlight = new ArrayList<>();

    public Passenger() {
    }

    public Passenger(String firstName, String lastName, String sex, String country, String notes, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.country = country;
        this.notes = notes;
        this.birthDate = birthDate;
    }

    public Passenger(Integer id, String firstName, String lastName, String sex, String country, String notes, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.country = country;
        this.notes = notes;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Flight> getListOfFlight() {
        return listOfFlight;
    }

    public void setListOfFlight(List<Flight> listOfFlight) {
        this.listOfFlight = listOfFlight;
    }


    public void assignFlight(Flight flight){

        listOfFlight.add(flight);
    }

    public void removeFlight(Flight flight){

        listOfFlight.remove(flight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id.equals(passenger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex=" + sex +
                ", Country='" + country + '\'' +
                ", Notes='" + notes + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
