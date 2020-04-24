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
public class PassengerController {


    private PassengerServiceImpl passengerDao;

    @Autowired
    public PassengerController(PassengerServiceImpl passengerDao) {
        this.passengerDao = passengerDao;
    }

    @GetMapping("/passengers")
    public ResponseEntity<List<Passenger>> getListOfFLights() {
        return ResponseEntity.ok(passengerDao.findAll());
    }

    @PostMapping(value = "/passenger")
    public ResponseEntity<Passenger> savePassenger(@RequestBody Passenger passenger){
        return new ResponseEntity<>(passengerDao.savePassenger(passenger), HttpStatus.CREATED) ;
    }

    @PutMapping(value = "/passenger")
    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger){
        return new ResponseEntity<>(passengerDao.updatePassenger(passenger), HttpStatus.OK);
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

    @GetMapping("/passenger/flights/{passengerId}")
    public ResponseEntity<List<Flight>> getPassengerFlights(@PathVariable("passengerId") Integer id){
        return ResponseEntity.ok(passengerDao.listOfPassengerFlights(id));
    }

    @DeleteMapping("/passenger/flights/{passengerId}/{flightId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlightFromPassenger(@PathVariable("passengerId") Integer passengerId,
                                          @PathVariable("flightId") Integer flightId){
        passengerDao.deleteFlightFromPassenger(flightId, passengerId);

    }

    @PutMapping("/passenger/flights/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFlightToPassenger(@PathVariable("passengerId") Integer passengerId,
                                     @RequestBody int[] flightsId){
        passengerDao.addFlightsToPassenger(flightsId, passengerId);

    }
}
