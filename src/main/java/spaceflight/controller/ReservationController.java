package spaceflight.controller;

import spaceflight.model.Flight;
import spaceflight.service.SearchingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
public class ReservationController {


    @Autowired
    SearchingServiceImpl searchingService;

    @PostMapping(path = "/findFlight")
    public ResponseEntity<List<Flight>> getFindFlights(@RequestBody HashMap<String, String> searchParams){

        List<Flight> flightList = searchingService.getFoundedFlights(searchParams);
        return ResponseEntity.ok(flightList);
    }



}
