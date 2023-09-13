package com.qualco.nationsapp.util.exception;

public class DataAccessLayerException extends RuntimeException{

    public DataAccessLayerException(){
        super("A data access layer exception occurred.");
    }
}
