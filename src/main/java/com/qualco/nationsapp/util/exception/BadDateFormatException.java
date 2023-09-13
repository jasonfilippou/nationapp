package com.qualco.nationsapp.util.exception;

/**
 * A {@link RuntimeException} thrown in cases of a bad date format provided by the user.
 *
 * @author jason
 */
public class BadDateFormatException extends RuntimeException {

    public BadDateFormatException(String msg) {
        super(msg);
    }
}
