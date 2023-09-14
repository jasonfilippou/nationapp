package com.qualco.nationsapp.model.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A simple POJO to answer task 3 of the instructions.
 *
 * @author jason
 */
@Data
@NoArgsConstructor
public class StatsEntry {
    private String continentName;
    private String regionName;
    private String countryName;
    private Integer year;
    private Long population;
    private BigDecimal gdp;
}
