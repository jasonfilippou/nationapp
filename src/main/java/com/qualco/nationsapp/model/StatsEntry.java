package com.qualco.nationsapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class StatsEntry {
    private String continentName;
    private String regionName;
    private Integer year;
    private Long population;
    private BigDecimal gdp;
}
