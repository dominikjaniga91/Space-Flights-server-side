package spaceflight.service;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;

import java.util.List;
import java.util.Map;

public interface PassengerService {

    List<Passenger> findAll();

    Passenger savePassenger(Passenger passenger);

    void deletePassengerById(Integer id);

    Passenger getPassengerById(Integer id);

    List<Flight> listOfPassengerFlights(Integer id);

    Passenger updatePassenger(Passenger passenger);

    void addFlightsToPassenger(int passengerId, int... flights);

    void deleteFlightFromPassenger(int flightId, int passengerId);

    List<Map<String, Object>> getPassengersAsListOfMaps();
}
