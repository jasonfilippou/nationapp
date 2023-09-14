package com.qualco.nationsapp.util.exception;

import lombok.Getter;

/**
 * Α {@link RuntimeException} thrown by our application when a query based on a provided country could not find
 * any associated data.
 *
 * @author jason
 */
@Getter
public class CountryNotFoundException extends RuntimeException{

    private final String country;
    public CountryNotFoundException(String country){
        super("Country " + country + " not found in database.");
        this.country = country;
    }
}
