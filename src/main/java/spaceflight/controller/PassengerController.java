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


    private FlightServiceImpl flightDao;
    private PassengerServiceImpl passengerDao;

    @Autowired
    public PassengerController(FlightServiceImpl flightDao, PassengerServiceImpl passengerDao) {
        this.flightDao = flightDao;
        this.passengerDao = passengerDao;
    }

    @GetMapping("/allPassengers")
    public ResponseEntity<List<Passenger>> getListOfFLights() {
        return ResponseEntity.ok(passengerDao.findAll());
    }

    @PostMapping(value = "/savePassenger")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTourist(@RequestBody Passenger passenger){
        passengerDao.savePassenger(passenger);
    }

    @PutMapping(value = "/updatePassenger")
    @ResponseStatus(HttpStatus.OK)
    public void updateTourist(@RequestBody Passenger passenger){
        passengerDao.updatePassenger(passenger);
    }

    @DeleteMapping("/deletePassenger{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePassenger(@PathVariable("id") Integer id){
        passengerDao.deletePassengerById(id);
    }

    @GetMapping("/loadPassenger{id}")
    public ResponseEntity<Passenger> loadPassenger(@PathVariable("id") Integer id){

        return ResponseEntity.ok(passengerDao.getPassengerById(id));
    }

    @GetMapping("/passengerFlights{id}")
    public ResponseEntity<List<Flight>> getPassengerFlights(@PathVariable("id") Integer id){
        return ResponseEntity.ok(passengerDao.listOfPassengerFlights(id));
    }

    @DeleteMapping("/deleteFromPassenger")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlightFromPassenger(@RequestParam("flightId") String flightId,
                                          @RequestParam("passengerId") String passengerId){
        passengerDao.deleteFlightFromPassenger(flightId, passengerId);

    }

    @PostMapping("/addFlightToPassenger")
    @ResponseStatus(HttpStatus.OK)
    public void addFlightToPassenger(@RequestParam("passengerId") String passengerId,
                                     @RequestBody int[] flightsId){
        passengerDao.addPassengerToFlight(flightsId, passengerId);

    }
}
