package spaceflight.model.dto;

import java.time.LocalDate;

public class FlightDto {

    private Integer id;
    private String destination;
    private LocalDate startDate;
    private LocalDate finishDate;
    private Integer numberOfSeats;
    private int amountOfPassengers;
    private double ticketPrice;

    public FlightDto() {
    }

    public FlightDto(Integer id, String destination, LocalDate startDate, LocalDate finishDate, Integer numberOfSeats, int amountOfPassengers, double ticketPrice) {
        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.numberOfSeats = numberOfSeats;
        this.amountOfPassengers = amountOfPassengers;
        this.ticketPrice = ticketPrice;
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

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getAmountOfPassengers() {
        return amountOfPassengers;
    }

    public void setAmountOfPassengers(int amountOfPassengers) {
        this.amountOfPassengers = amountOfPassengers;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
