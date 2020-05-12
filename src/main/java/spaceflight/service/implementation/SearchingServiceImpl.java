package spaceflight.service.implementation;

import spaceflight.exception.InvalidSearchFlightDataException;
import spaceflight.exception.InvalidSearchPassengerDataException;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.repository.FlightRepositoryImpl;
import spaceflight.repository.PassengerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class SearchingServiceImpl {

    @Autowired
    FlightRepositoryImpl flightDao;

    @Autowired
    PassengerRepositoryImpl passengerDao;


    public List<Flight> getFoundedFlights(HashMap<String, String> searchParams){

        System.out.println(searchParams);

        List<Flight> flightList;
        LocalDate startDate = searchParams.containsKey("startDate") ? LocalDate.parse(searchParams.get("startDate")) : null;
        LocalDate finishDate = searchParams.containsKey("finishDate") ? LocalDate.parse(searchParams.get("finishDate")) : null;
        String destination = searchParams.getOrDefault("destination", null);

        if(destination != null && startDate != null && finishDate != null){
            flightList = flightDao.getFlightsByDestinationAndStartDateAndFinishDate(destination, startDate, finishDate);
        }else if(destination != null && startDate == null && finishDate == null){
            flightList = flightDao.getFlightsByDestination(destination);
        }else if(destination == null && startDate != null && finishDate != null){
            flightList = flightDao.getFlightsByStartDateAndFinishDate(startDate, finishDate);
        }else if(destination != null && startDate != null){
            flightList = flightDao.getFlightsByDestinationAndStartDateIsGreaterThanEqual(destination, startDate);
        } else{
            throw new InvalidSearchFlightDataException(startDate, finishDate, destination);
        }

        return flightList;
    }


    public List<Passenger> getFoundedPassengers(HashMap<String, String> searchParams){

        List<Passenger> passengerList;
        LocalDate birthDate = searchParams.containsKey("birthDate") ? LocalDate.parse(searchParams.get("birthDate")) : null;
        String firstName = searchParams.getOrDefault("firstName", null);
        String lastName = searchParams.getOrDefault("lastName", null);

        if(birthDate != null && firstName != null && lastName != null){
            passengerList = passengerDao.getPassengersByFirstNameAndLastNameAndBirthDate(firstName, lastName, birthDate);
        }else if(birthDate != null && firstName == null && lastName == null){
            passengerList = passengerDao.getPassengersByBirthDate(birthDate);
        }else if(birthDate == null && firstName != null && lastName != null){
            passengerList = passengerDao.getPassengersByFirstNameAndLastName(firstName, lastName);
        }else if(birthDate == null && firstName == null && lastName != null){
            passengerList = passengerDao.getPassengersByLastName(lastName);
        }else if(birthDate == null && firstName != null){
            passengerList = passengerDao.getPassengersByFirstName(firstName);
        }else{
            throw new InvalidSearchPassengerDataException(firstName, lastName, birthDate);
        }

        return passengerList;
    }
}
