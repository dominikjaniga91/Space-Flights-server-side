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

        Passenger passenger = passengerDao.findById(id)
                .orElseThrow(()->new PassengerNotFoundException(id));
       return passenger.getListOfFlight();
    }


    @Override
    public void addFlightsToPassenger(int[] flights, int passengerId){

        Passenger passenger = passengerDao.getPassengerById(passengerId)
                .orElseThrow(() -> new PassengerNotFoundException(passengerId));

        for (int id : flights) {
            Flight flight = flightDao.getFlightById(id)
                    .orElseThrow(() -> new FlightNotFoundException(id));
            if(flight.getAmountOfPassengers() <= flight.getNumberOfSeats() &&
               !passenger.getListOfFlight().contains(flight)){

                passenger.assignFlight(flight);
                flightDao.save(flight);

            }
        }
    }

    @Override
    public void deleteFlightFromPassenger(int flightId, int passengerId){

        Flight flight = flightDao.getFlightById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));

        Passenger passenger = passengerDao.getPassengerById(passengerId)
                .orElseThrow(() -> new PassengerNotFoundException(passengerId));

        passenger.removeFlight(flight);
        flightDao.save(flight);

    }
}
