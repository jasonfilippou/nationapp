package com.qualco.nationsapp.model.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A simple POJO to answer task 1(a) of the instructions.
 *
 * @author jason
 */
@Data
@NoArgsConstructor
public class BasicCountryEntry {
    private String name;
    private BigDecimal area;
    private String countryCode;
}
