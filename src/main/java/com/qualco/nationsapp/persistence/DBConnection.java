package com.qualco.nationsapp.persistence;

import com.qualco.nationsapp.model.tasks.BasicCountryEntry;
import com.qualco.nationsapp.model.tasks.CountryWithMaxGDPPerCapitaEntry;
import com.qualco.nationsapp.model.tasks.StatsEntry;
import com.qualco.nationsapp.util.PaginatedQueryParams;

import java.util.List;

public interface DBConnection {

    List<BasicCountryEntry> getBasicCountryInfo(PaginatedQueryParams params);

    List<String> getLanguagesOfCountry(String country);

    List<CountryWithMaxGDPPerCapitaEntry> getMaxGDPPerCapita(PaginatedQueryParams params);

    List<StatsEntry> getStats(PaginatedQueryParams params);
}
