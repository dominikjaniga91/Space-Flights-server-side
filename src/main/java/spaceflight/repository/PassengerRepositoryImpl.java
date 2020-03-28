package spaceflight.repository;

import spaceflight.model.Flight;
import spaceflight.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepositoryImpl extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> getPassengerById(Integer id);

    Passenger findByFirstName(String name);
    

 }
