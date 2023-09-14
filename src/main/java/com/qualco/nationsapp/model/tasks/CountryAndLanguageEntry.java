package com.qualco.nationsapp.model.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A simple POJO to answer task 1(b) of the instructions.
 *
 * @author jason
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryAndLanguageEntry {
    private String country;
    private String language;
}
