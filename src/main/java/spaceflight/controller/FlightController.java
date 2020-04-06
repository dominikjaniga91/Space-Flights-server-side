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

    @Autowired
    PassengerServiceImpl passengerDao;

    @Autowired
    FlightServiceImpl flightDao;


    @GetMapping("/allFlights")
    public ResponseEntity<List<Flight>> getListOfFLights() {
        return ResponseEntity.ok(flightDao.findAll());
    }

    @PostMapping(value = "/saveFlight")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFlight(@RequestBody Flight flight) {
        flightDao.saveFlight(flight);
    }

    @PutMapping(value = "/updateFlight")
    @ResponseStatus(HttpStatus.OK)
    public void updateFlight(@RequestBody Flight flight) {
        flightDao.updateFlight(flight);
    }

    @DeleteMapping("/deleteFlight{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable("id") Integer id){
        flightDao.deleteFlightById(id);
    }

    @GetMapping("/loadFlight{id}")
    public ResponseEntity<Flight> loadPassenger(@PathVariable("id") Integer id){
        return ResponseEntity.ok(flightDao.getFlightById(id));
    }

    @GetMapping("/flightPassengers{id}")
    public ResponseEntity<List<Passenger>> getPassengerFlights(@PathVariable("id") Integer id){
        return ResponseEntity.ok(flightDao.listOfPassengers(id));
    }

    @DeleteMapping("/deleteFromFlight")
    @ResponseStatus(HttpStatus.OK)
    public void deletePassengerFromFlight(@RequestParam("flightId") String flightId,
                                          @RequestParam("passengerId") String passengerId){
        flightDao.deletePassengerFromFlight(flightId, passengerId);
    }

    @GetMapping("/addPassengerToFlight")
    @ResponseStatus(HttpStatus.OK)
    public void addPassengerToFlight(@RequestParam("flightId") String flightId,
                                     @RequestParam("passengerId") String passengerId){
        flightDao.addPassengerToFlight(flightId, passengerId);
    }

}
