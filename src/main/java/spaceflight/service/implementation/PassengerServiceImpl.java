package spaceflight.service.implementation;

import spaceflight.exception.FlightNotFoundException;
import spaceflight.exception.PassengerNotFoundException;
import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.repository.FlightRepositoryImpl;
import spaceflight.repository.PassengerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spaceflight.service.PassengerService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PassengerServiceImpl implements PassengerService {

    private PassengerRepositoryImpl passengerDao;
    private FlightRepositoryImpl flightDao;

    @Autowired
    public PassengerServiceImpl(PassengerRepositoryImpl passengerDao,
                                FlightRepositoryImpl flightDao) {
        this.passengerDao = passengerDao;
        this.flightDao = flightDao;
    }

    @Override
    public Passenger updatePassenger(Passenger passenger) {

        Passenger tmpPassenger = passengerDao.getPassengerById(passenger.getId())
                .orElseThrow(() -> new PassengerNotFoundException(passenger.getId()));

        tmpPassenger.setFirstName(passenger.getFirstName());
        tmpPassenger.setLastName(passenger.getLastName());
        tmpPassenger.setSex(passenger.getSex());
        tmpPassenger.setCountry(passenger.getCountry());
        tmpPassenger.setNotes(passenger.getNotes());
        tmpPassenger.setBirthDate(passenger.getBirthDate());

        return passengerDao.save(tmpPassenger);
    }

    @Override
    public List<Passenger> findAll(){
        return passengerDao.findAll();
    }

    @Override
    public List<Map<String, Object>> getPassengersAsListOfMaps(){

        List<Passenger> passengers = passengerDao.findAll();

        List<Map<String, Object>> listOfPassengers = new ArrayList<>();

        passengers.forEach(passenger -> {

            Map<String, Object> passengerMap = new LinkedHashMap<>();
            passengerMap.put("ID", passenger.getId());
            passengerMap.put("First name", passenger.getFirstName());
            passengerMap.put("last name", passenger.getLastName());
            passengerMap.put("Sex", passenger.getSex());
            passengerMap.put("Country", passenger.getCountry());
            passengerMap.put("Notes", passenger.getNotes());
            passengerMap.put("Birth date", passenger.getBirthDate());

            listOfPassengers.add(passengerMap);
        });

        return listOfPassengers;
    }

    @Override
    public Passenger savePassenger(Passenger passenger) {
        return passengerDao.save(passenger);
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
    public void addFlightsToPassenger(int passengerId, int... flights){

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
