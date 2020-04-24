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

    private FlightRepositoryImpl flightDao;
    private PassengerRepositoryImpl passengerDao;

    @Autowired
    public FlightServiceImpl(FlightRepositoryImpl flightDao, PassengerRepositoryImpl passengerDao) {
        this.flightDao = flightDao;
        this.passengerDao = passengerDao;
    }

    @Override
    public Flight updateFlight(Flight flight) {

        Flight tmpFlight = flightDao.getFlightById(flight.getId())
                .orElseThrow(() -> new FlightNotFoundException(flight.getId()));

        tmpFlight.setDestination(flight.getDestination());
        tmpFlight.setStartDate(flight.getStartDate());
        tmpFlight.setFinishDate(flight.getFinishDate());
        tmpFlight.setNumberOfSeats(flight.getNumberOfSeats());
        tmpFlight.setTicketPrice(flight.getTicketPrice());

        return flightDao.save(tmpFlight);
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return flightDao.save(flight);
    }

    @Override
    public List<Flight> findAll(){
        return flightDao.findAll();
    }

    @Override
    public Flight getFlightById(int id) {
        return flightDao.findById(id)
                .orElseThrow(()->new FlightNotFoundException(id));
    }

    @Override
    public void deleteFlightById(int id) {
        Flight flight = flightDao.findById(id)
                .orElseThrow(()->new PassengerNotFoundException(id));
        flightDao.delete(flight);
    }

    @Override
    public List<Passenger> listOfPassengers(int id) {
        Flight flight = flightDao.findById(id).
                orElseThrow(()->new FlightNotFoundException(id));
        return flight.getListOfPassengers();
    }


    @Override
    public void addPassengersToFlight(int flightId, int... passengers){

        Flight flight = flightDao.getFlightById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));

        for (int id : passengers) {
            Passenger passenger = passengerDao.getPassengerById(id)
                    .orElseThrow(() -> new PassengerNotFoundException(id));

            if(flight.getAmountOfPassengers() <= flight.getNumberOfSeats() &&
               !passenger.getListOfFlight().contains(flight)) {

                flight.assignPassenger(passenger);
                flightDao.save(flight);
            }
        }
    }

    @Override
    public void deletePassengerFromFlight(int flightId, int passengerId){

        Flight flight = flightDao.getFlightById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));

        Passenger passenger = passengerDao.getPassengerById(passengerId)
                .orElseThrow(() -> new PassengerNotFoundException(passengerId));

        flight.removePassenger(passenger);
        flightDao.save(flight);

    }
}
