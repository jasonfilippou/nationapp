package com.qualco.nationsapp.util.exception;

import lombok.Getter;

@Getter
public class CountryNotFoundException extends RuntimeException{

    private final String country;
    public CountryNotFoundException(String country){
        super("Country " + country + " not found in database.");
        this.country = country;
    }
}
