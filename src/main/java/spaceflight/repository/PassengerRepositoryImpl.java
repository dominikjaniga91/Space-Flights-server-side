package spaceflight.repository;

import spaceflight.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepositoryImpl extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> getPassengerById(Integer id);

    List<Passenger> getPassengersByFirstName(String firstName);

    List<Passenger> getPassengersByLastName(String lastName);

    List<Passenger> getPassengersByFirstNameAndLastName(String firstName, String lastName);

    List<Passenger> getPassengersByFirstNameAndLastNameAndBirthDate (String firstName, String lastName, LocalDate birthDate);
    
    List<Passenger> getPassengersByBirthDate(LocalDate birthDate);
 }
