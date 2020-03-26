package com.spaceflights.spaceflights.repository;

import com.spaceflights.spaceflights.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepositoryImpl extends JpaRepository<Flight, Integer> {

        Optional<Flight> getFlightById(Integer id);

        List<Flight> getFlightsByDestination(String destination);

        List<Flight> getFlightsByDestinationAndStartDateAndFinishDate(String destination, LocalDate startDate, LocalDate finishDate);

        List<Flight> getFlightsByStartDateAndFinishDate(LocalDate startDate, LocalDate finishDate);

        List<Flight> getFlightsByDestinationAndStartDateIsGreaterThanEqual(String destination, LocalDate startDate);


}
