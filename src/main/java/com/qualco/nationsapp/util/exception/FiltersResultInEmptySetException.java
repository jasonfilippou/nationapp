package com.qualco.nationsapp.util.exception;

import com.google.common.base.Joiner;
import lombok.Getter;

import java.util.Map;

@Getter
public class FiltersResultInEmptySetException extends RuntimeException{

    private final Map<String, String> filters;

    public FiltersResultInEmptySetException(Map<String, String> filters){
        super("Filters: " +  Joiner.on(",").withKeyValueSeparator("=").join(filters) + " resulted in no results.");
        this.filters = filters;
    }
}
