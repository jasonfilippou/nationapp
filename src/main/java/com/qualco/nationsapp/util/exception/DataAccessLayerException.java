package com.qualco.nationsapp.util.exception;

import lombok.Getter;

/**
 * A {@link RuntimeException} thrown by our persistence layer in case of unrecoverable database errors.
 *
 * @author jason
 */
@Getter
public class DataAccessLayerException extends RuntimeException{

    public DataAccessLayerException(){
        super("A data access layer exception occurred.");
    }
}
