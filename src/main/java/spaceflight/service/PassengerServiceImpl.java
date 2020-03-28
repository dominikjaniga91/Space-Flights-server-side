package spaceflight.service;

import spaceflight.exception.FlightNotFoundException;
import spaceflight.exception.PassengerNotFoundException;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.repository.FlightRepositoryImpl;
import spaceflight.repository.PassengerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    PassengerRepositoryImpl passengerDao;

    @Autowired
    FlightRepositoryImpl flightDao;

    @Override
    public void updatePassenger(Passenger passenger) {

        Passenger tmpPassenger = passengerDao.getPassengerById(passenger.getId())
                .orElseThrow(() -> new PassengerNotFoundException(passenger.getId()));

        tmpPassenger.setFirstName(passenger.getFirstName());
        tmpPassenger.setLastName(passenger.getLastName());
        tmpPassenger.setSex(passenger.getSex());
        tmpPassenger.setCountry(passenger.getCountry());
        tmpPassenger.setNotes(passenger.getNotes());
        tmpPassenger.setBirthDate(passenger.getBirthDate());

        passengerDao.save(tmpPassenger);
    }

    @Override
    public List<Passenger> findAll(){
        return passengerDao.findAll();
    }

    @Override
    public void savePassenger(Passenger passenger) {
        passengerDao.save(passenger);
    }

    @Override
    public Passenger getPassengerById(Integer id) {
        return  passengerDao.findById(id)
                .orElseThrow(()->new PassengerNotFoundException(id));
    }

    @Override
    public void deletePassengerById(Integer id) {
        Passenger passenger = passengerDao.findById(id)
                .orElseThrow(()->new PassengerNotFoundException(id));
        passengerDao.delete(passenger);
    }



    @Override
    public List<Flight> listOfPassengerFlights(Integer id) {

        Passenger passenger = passengerDao.findById(id).orElseThrow(
                ()->new PassengerNotFoundException(id));
       return passenger.getListOfFlight();
    }


    @Override
    public void addPassengerToFlight(String flightId, String passengerId){

        int tempFlightId = Integer.parseInt(flightId);
        int tempPassengerId = Integer.parseInt(passengerId);

            Flight tmpFlight = flightDao.getFlightById(tempFlightId)
                    .orElseThrow(() -> new FlightNotFoundException(tempFlightId));

            Passenger tmpPassenger = passengerDao.getPassengerById(tempPassengerId)
                    .orElseThrow(() -> new PassengerNotFoundException(tempPassengerId));

            if(tmpFlight.getAvailableSeats() > 0 && !tmpPassenger.getListOfFlight().contains(tmpFlight)){
                tmpFlight.decrementAvailableSeats();
                tmpPassenger.assignFlight(tmpFlight);
                flightDao.save(tmpFlight);

            }
    }

    @Override
    public void deleteFlightFromPassenger(String flightId, String passengerId){

        int tempFlightId = Integer.parseInt(flightId);
        int tempPassengerId = Integer.parseInt(passengerId);

        Flight tmpFlight = flightDao.getFlightById(tempFlightId)
                .orElseThrow(() -> new FlightNotFoundException(tempFlightId));

        Passenger tmpPassenger = passengerDao.getPassengerById(tempPassengerId)
                .orElseThrow(() -> new PassengerNotFoundException(tempPassengerId));

        tmpPassenger.removeFlight(tmpFlight);
        tmpFlight.incrementAvailableSeats();
        flightDao.save(tmpFlight);

    }
}
