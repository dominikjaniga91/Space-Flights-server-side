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
@RequestMapping("/flights")
public class FlightController {


    private PassengerServiceImpl passengerDao;
    private FlightServiceImpl flightDao;

    @Autowired
    public FlightController(PassengerServiceImpl passengerDao, FlightServiceImpl flightDao) {
        this.passengerDao = passengerDao;
        this.flightDao = flightDao;
    }

    @GetMapping("/flight")
    public ResponseEntity<List<Flight>> getListOfFLights() {
        return ResponseEntity.ok(flightDao.findAll());
    }

    @PostMapping(value = "/flight")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFlight(@RequestBody Flight flight) {
        flightDao.saveFlight(flight);
    }

    @PutMapping(value = "/flight")
    @ResponseStatus(HttpStatus.OK)
    public void updateFlight(@RequestBody Flight flight) {
        flightDao.updateFlight(flight);
    }

    @DeleteMapping("/flight/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable("id") Integer id){
        flightDao.deleteFlightById(id);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Flight> loadPassenger(@PathVariable("flightId") Integer id){
        return ResponseEntity.ok(flightDao.getFlightById(id));
    }

    @GetMapping("/flight/{flightId}/passengers")
    public ResponseEntity<List<Passenger>> getPassengerFlights(@PathVariable("flightId") Integer id){
        return ResponseEntity.ok(flightDao.listOfPassengers(id));
    }

    @DeleteMapping("/flight/{flightId}/passengers/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePassengerFromFlight(@PathVariable("flightId") Integer flightId,
                                          @PathVariable("passengerId") Integer passengerId){
        flightDao.deletePassengerFromFlight(flightId, passengerId);
    }

    @PutMapping("/flight/{flightId}/passengers")
    @ResponseStatus(HttpStatus.OK)
    public void addPassengersToFlight(@PathVariable("flightId") Integer flightId,
                                     @RequestBody int[] passengers){
        flightDao.addPassengersToFlight(flightId, passengers);
    }

}
