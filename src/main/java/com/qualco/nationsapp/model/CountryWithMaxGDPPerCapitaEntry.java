package com.qualco.nationsapp.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CountryWithMaxGDPPerCapitaEntry {
    private String name;
    private String countryCode;
    private BigDecimal maxGDPPerCapita;
}
