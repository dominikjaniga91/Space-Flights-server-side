package spaceflight.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import spaceflight.model.validation.StartFinishDate;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="flights")
@StartFinishDate
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="destination")
    @NotNull
    private String destination;

    @Column(name="start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @Column(name="finish_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate finishDate;

    @Column(name="number_of_seats")
    @NotNull
    private int numberOfSeats;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name="passengers_flights",
            joinColumns = @JoinColumn(name="flight_id", updatable = false),
            inverseJoinColumns = @JoinColumn(name="passenger_id", updatable = false))
    private List<Passenger> listOfPassengers = new ArrayList<>();

    @Column(name="amount_of_passengers")
    private int amountOfPassengers;

    @Column(name="ticket_price")
    @NotNull
    private double ticketPrice;

    public Flight() {
    }

    public Flight(String destination, LocalDate startDate, LocalDate finishDate, int numberOfSeats, double ticketPrice) {
        this.destination = destination;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.numberOfSeats = numberOfSeats;
        this.ticketPrice = ticketPrice;
        this.amountOfPassengers = numberOfSeats;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public List<Passenger> getListOfPassengers() {
        return listOfPassengers;
    }

    public void setListOfPassengers(List<Passenger> listOfPassengers) {
        this.listOfPassengers = listOfPassengers;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAmountOfPassengers() {
        return amountOfPassengers;
    }

    public void setAmountOfPassengers(int amountOfPassengers) {
        this.amountOfPassengers = amountOfPassengers;
    }

    public void assignPassenger(Passenger passenger){
        listOfPassengers.add(passenger);
        this.amountOfPassengers++;
    }

    public void removePassenger(Passenger passenger) {
        listOfPassengers.remove(passenger);
        this.amountOfPassengers--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return id.equals(flight.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Flight{" +
                "destination='" + destination + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", numberOfSeats=" + numberOfSeats +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
