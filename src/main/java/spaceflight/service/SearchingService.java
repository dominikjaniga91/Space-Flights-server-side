package spaceflight.service;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;

import java.util.HashMap;
import java.util.List;

public interface SearchingService {

    List<Flight> getFoundedFlights(HashMap<String, String> searchParams);

    List<Passenger> getFoundedPassengers(HashMap<String, String> searchParams);

}
