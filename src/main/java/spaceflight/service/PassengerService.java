package spaceflight.service;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;

import java.util.List;

public interface PassengerService {

    List<Passenger> findAll();

    void savePassenger(Passenger passenger);

    void deletePassengerById(Integer id);

    Passenger getPassengerById(Integer id);

    List<Flight> listOfPassengerFlights(Integer id);

    void updatePassenger(Passenger passenger);

    void addFlightsToPassenger(int[] flightsId, int passengerId);

    void deleteFlightFromPassenger(int flightId, int passengerId);
}
