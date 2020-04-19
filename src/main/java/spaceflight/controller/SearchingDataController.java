package spaceflight.controller;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import spaceflight.service.SearchingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/search")
public class SearchingDataController {


    private SearchingServiceImpl searchingService;

    @Autowired
    public SearchingDataController(SearchingServiceImpl searchingService) {
        this.searchingService = searchingService;
    }

    @PostMapping(path = "/flight")
    public ResponseEntity<List<Flight>> getFoundedFlights(@RequestBody HashMap<String, String> searchParams){

        List<Flight> flightList = searchingService.getFoundedFlights(searchParams);
        return ResponseEntity.ok(flightList);
    }
    @PostMapping(path = "/passenger")
    public ResponseEntity<List<Passenger>> getFoundedPassengers(@RequestBody HashMap<String, String> searchParams){

        List<Passenger> passengerList = searchingService.getFoundedPassengers(searchParams);
        return ResponseEntity.ok(passengerList);
    }

}
