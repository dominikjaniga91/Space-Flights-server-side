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
public class FlightServiceImpl implements FlightService{

    @Autowired
    FlightRepositoryImpl flightDao;

    @Autowired
    PassengerRepositoryImpl passengerDao;

    @Override
    public void updateFlight(Flight flight) {

        Flight tmpFlight = flightDao.getFlightById(flight.getId())
                .orElseThrow(() -> new FlightNotFoundException(flight.getId()));

        tmpFlight.setDestination(flight.getDestination());
        tmpFlight.setStartDate(flight.getStartDate());
        tmpFlight.setFinishDate(flight.getFinishDate());
        tmpFlight.setNumberOfSeats(flight.getNumberOfSeats());
        tmpFlight.setTicketPrice(flight.getTicketPrice());

        flightDao.save(tmpFlight);
    }

    @Override
    public void saveFlight(Flight flight) {
        flightDao.save(flight);
    }

    @Override
    public List<Flight> findAll(){
        return flightDao.findAll();
    }

    @Override
    public Flight getFlightById(Integer id) {
        return flightDao.findById(id)
                .orElseThrow(()->new FlightNotFoundException(id));
    }

    @Override
    public void deleteFlightById(Integer id) {
        Flight flight = flightDao.findById(id)
                .orElseThrow(()->new PassengerNotFoundException(id));
        flightDao.delete(flight);
    }

    @Override
    public List<Passenger> listOfPassengers(Integer id) {
        Flight flight = flightDao.findById(id).
                orElseThrow(()->new FlightNotFoundException(id));
        return flight.getListOfPassengers();
    }


    @Override
    public void addPassengerToFlight(String flightId, String passengerId){

        int tempFlightId = Integer.parseInt(flightId);
        int tempPassengerId = Integer.parseInt(passengerId);

        Flight tmpFlight = flightDao.getFlightById(tempFlightId)
                .orElseThrow(() -> new FlightNotFoundException(tempFlightId));

        Passenger tmpPassenger = passengerDao.getPassengerById(tempPassengerId)
                .orElseThrow(() -> new PassengerNotFoundException(tempPassengerId));

        if(tmpFlight.getAmountOfPassengers() < tmpFlight.getNumberOfSeats() && !tmpFlight.getListOfPassengers().contains(tmpPassenger)){
            tmpFlight.assignPassenger(tmpPassenger);
            flightDao.save(tmpFlight);
        }
    }

    @Override
    public void deletePassengerFromFlight(String flightId, String passengerId){

        int tempFlightId = Integer.parseInt(flightId);
        int tempPassengerId = Integer.parseInt(passengerId);

        Flight tmpFlight = flightDao.getFlightById(tempFlightId)
                .orElseThrow(() -> new FlightNotFoundException(tempFlightId));

        Passenger tmpPassenger = passengerDao.getPassengerById(tempPassengerId)
                .orElseThrow(() -> new PassengerNotFoundException(tempPassengerId));

        tmpFlight.removePassenger(tmpPassenger);
        flightDao.save(tmpFlight);

    }
}
