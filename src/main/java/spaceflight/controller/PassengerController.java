package spaceflight.controller;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/passengers")
public class PassengerController {


    private FlightServiceImpl flightDao;
    private PassengerServiceImpl passengerDao;

    @Autowired
    public PassengerController(FlightServiceImpl flightDao, PassengerServiceImpl passengerDao) {
        this.flightDao = flightDao;
        this.passengerDao = passengerDao;
    }

    @GetMapping("/")
    public ResponseEntity<List<Passenger>> getListOfFLights() {
        return ResponseEntity.ok(passengerDao.findAll());
    }

    @PostMapping(value = "/passenger")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTourist(@RequestBody Passenger passenger){
        passengerDao.savePassenger(passenger);
    }

    @PutMapping(value = "/passenger")
    @ResponseStatus(HttpStatus.OK)
    public void updateTourist(@RequestBody Passenger passenger){
        passengerDao.updatePassenger(passenger);
    }

    @DeleteMapping("/passenger/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePassenger(@PathVariable("passengerId") Integer id){
        passengerDao.deletePassengerById(id);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<Passenger> loadPassenger(@PathVariable("passengerId") Integer id){
        return ResponseEntity.ok(passengerDao.getPassengerById(id));
    }

    @GetMapping("/passenger/{passengerId}/flights")
    public ResponseEntity<List<Flight>> getPassengerFlights(@PathVariable("passengerId") Integer id){
        return ResponseEntity.ok(passengerDao.listOfPassengerFlights(id));
    }

    @DeleteMapping("/passenger/{passengerId}/flight/{flightId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlightFromPassenger(@PathVariable("passengerId") Integer passengerId,
                                          @PathVariable("flightId") Integer flightId){
        passengerDao.deleteFlightFromPassenger(flightId, passengerId);

    }

    @PutMapping("/passenger/{passengerId}/flights")
    @ResponseStatus(HttpStatus.OK)
    public void addFlightToPassenger(@PathVariable("passengerId") Integer passengerId,
                                     @RequestBody int[] flightsId){
        passengerDao.addFlightsToPassenger(flightsId, passengerId);

    }
}
