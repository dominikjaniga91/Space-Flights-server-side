package spaceflight.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartFinishDateValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartFinishDate {

    String message() default  "Data is incorrect";

    Class<?>[] payload() default {};

    Class<? extends Payload>[] groups() default {};




}
