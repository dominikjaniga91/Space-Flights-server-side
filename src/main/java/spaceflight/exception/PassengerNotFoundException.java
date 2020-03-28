package spaceflight.exception;

public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException(Integer id){

        super("Passenger ID: " + id + " doesn't exist");
    }
}
