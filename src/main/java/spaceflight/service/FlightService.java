package spaceflight.service;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;

import java.util.List;

public interface FlightService {

    List<Flight> findAll();

    void saveFlight(Flight flight);

    Flight getFlightById(Integer id);

    void deleteFlightById(Integer id);

    List<Passenger> listOfPassengers(Integer id);

    void updateFlight(Flight flight);

    void addPassengerToFlight(String flightId, String passengerId);

    void deletePassengerFromFlight(String flightId, String passengerId);

}
