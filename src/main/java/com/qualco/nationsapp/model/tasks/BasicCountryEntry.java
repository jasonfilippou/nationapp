package com.qualco.nationsapp.model.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BasicCountryEntry {
    private String name;
    private BigDecimal area;
    private String countryCode;
}
