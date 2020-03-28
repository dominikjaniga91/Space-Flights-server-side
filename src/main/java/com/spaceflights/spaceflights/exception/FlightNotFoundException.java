package com.spaceflights.spaceflights.exception;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(Integer id) {
        super("Flight ID: " + id + " doesn't exist");
    }
}
