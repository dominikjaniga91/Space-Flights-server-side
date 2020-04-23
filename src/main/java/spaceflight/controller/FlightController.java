package spaceflight.controller;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.service.FlightServiceImpl;
import spaceflight.service.PassengerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class FlightController {


    private PassengerServiceImpl passengerDao;
    private FlightServiceImpl flightDao;

    @Autowired
    public FlightController(PassengerServiceImpl passengerDao, FlightServiceImpl flightDao) {
        this.passengerDao = passengerDao;
        this.flightDao = flightDao;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getListOfFLights() {
        return ResponseEntity.ok(flightDao.findAll());
    }

    @PostMapping("/flight")
    public ResponseEntity<Flight> saveFlight(@RequestBody Flight flight) {
        return new ResponseEntity<>(flightDao.saveFlight(flight), HttpStatus.CREATED);
    }

    @PutMapping("/flight")
    @ResponseStatus(HttpStatus.OK)
    public void updateFlight(@RequestBody Flight flight) {
        flightDao.updateFlight(flight);
    }

    @DeleteMapping("/flight/{flightId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable("flightId") Integer id){
        flightDao.deleteFlightById(id);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Flight> loadPassenger(@PathVariable("flightId") Integer id){
        return ResponseEntity.ok(flightDao.getFlightById(id));
    }

    @GetMapping("/flight/passengers/{flightId}")
    public ResponseEntity<List<Passenger>> getFlightPassengers(@PathVariable("flightId") Integer id){
        return ResponseEntity.ok(flightDao.listOfPassengers(id));
    }

    @DeleteMapping("/flight/passengers/{flightId}/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePassengerFromFlight(@PathVariable("flightId") Integer flightId,
                                          @PathVariable("passengerId") Integer passengerId){
        flightDao.deletePassengerFromFlight(flightId, passengerId);
    }

    @PutMapping("/flight/passengers/{flightId}")
    @ResponseStatus(HttpStatus.OK)
    public void addPassengersToFlight(@PathVariable("flightId") Integer flightId,
                                     @RequestBody int[] passengers){
        flightDao.addPassengersToFlight(flightId, passengers);
    }

}
