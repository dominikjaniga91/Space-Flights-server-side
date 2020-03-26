package com.spaceflights.spaceflights.service;

import com.spaceflights.spaceflights.model.Flight;
import com.spaceflights.spaceflights.model.Passenger;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface FlightService {

    List<Flight> findAll();

    void saveFlight(Flight flight);

    Flight getFlightById(Integer id);

    void deleteFlightById(Integer id);

    List<Passenger> listOfPassengers(Integer id);

    void updateFight(Flight flight);

    void addPassengerToFlight(String flightId, String passengerId);

    void deletePassengerFromFlight(String flightId, String passengerId);

}
