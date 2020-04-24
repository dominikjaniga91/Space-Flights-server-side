package spaceflight.service;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;

import java.util.List;

public interface FlightService {

    List<Flight> findAll();

    Flight saveFlight(Flight flight);

    Flight getFlightById(int id);

    void deleteFlightById(int id);

    List<Passenger> listOfPassengers(int id);

    Flight updateFlight(Flight flight);

    void addPassengersToFlight(int flightId, int[] passengers);

    void deletePassengerFromFlight(int flightId, int passengerId);

}
