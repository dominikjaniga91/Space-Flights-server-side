package spaceflight.model.validation;

import spaceflight.model.Flight;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartFinishDateValidator implements ConstraintValidator<StartFinishDate, Flight> {


    @Override
    public void initialize(StartFinishDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(Flight flight, ConstraintValidatorContext constraintValidatorContext) {

        return flight.getFinishDate().isAfter(flight.getStartDate());
    }
}
