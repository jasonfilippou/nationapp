package com.qualco.nationsapp.model.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryAndLanguageEntry {
    private String country;
    private String language;
}
