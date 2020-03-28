package spaceflight.service;

import spaceflight.exception.InvalidFlightDataException;
import spaceflight.exception.InvalidPassengerDataException;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.repository.FlightRepositoryImpl;
import spaceflight.repository.PassengerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchingServiceImpl {

    @Autowired
    FlightRepositoryImpl flightDao;

    @Autowired
    PassengerRepositoryImpl passengerDao;


    public List<Flight> getFoundedFlights(HashMap<String, String> searchParams){

        List<Flight> flightList;
        LocalDate startDate = null;
        LocalDate finishDate = null;
        String destination = null;

        for(Map.Entry<String,String > keys : searchParams.entrySet()){

            if(keys.getKey().equals("startDate")){
                startDate = keys.getValue().equals("") ? null : LocalDate.parse(keys.getValue());
            }
            if(keys.getKey().equals("finishDate")){
                finishDate = keys.getValue().equals("") ? null : LocalDate.parse(keys.getValue());
            }
            if(keys.getKey().equals("destination")){
                destination = keys.getValue().equals("") ? null : keys.getValue();
            }
        }

        if(destination != null && startDate != null && finishDate != null){
            System.out.println("moja metoda");
            flightList = flightDao.getFlightsByDestinationAndStartDateAndFinishDate(destination, startDate, finishDate);
        }else if(destination != null && startDate == null && finishDate == null){
            flightList = flightDao.getFlightsByDestination(destination);
        }else if(destination == null && startDate != null && finishDate != null){
            flightList = flightDao.getFlightsByStartDateAndFinishDate(startDate, finishDate);
        }else if(destination != null && startDate != null){
            flightList = flightDao.getFlightsByDestinationAndStartDateIsGreaterThanEqual(destination, startDate);
        } else{
            throw new InvalidFlightDataException(startDate, finishDate, destination);
        }

        return flightList;
    }


    public List<Passenger> getFoundedPassengers(HashMap<String, String> searchParams){

        List<Passenger> passengerList;
        LocalDate birthDate = LocalDate.parse(searchParams.get("birthDate"));
        String firstName = searchParams.get("firstName");
        String lastName = searchParams.get("lastName");

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
            throw new InvalidPassengerDataException(firstName, lastName, birthDate);
        }

        return passengerList;
    }
}
