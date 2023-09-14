package com.qualco.nationsapp.model.tasks;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A simple POJO to answer task 2 of the instructions.
 *
 * @author jason
 */
@Data
@NoArgsConstructor
public class MaxGDPPerCapitaEntry {
    private String name;
    private String countryCode;
    private BigDecimal maxGDPPerCapita;
}
