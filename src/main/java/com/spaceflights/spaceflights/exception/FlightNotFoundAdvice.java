package com.spaceflights.spaceflights.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FlightNotFoundAdvice {

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getFlightNotFoundExceptionHandler(FlightNotFoundException ex){

        return  ex.getMessage();
    }
}
