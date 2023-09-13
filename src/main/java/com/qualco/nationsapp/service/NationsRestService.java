package com.qualco.nationsapp.service;

import com.qualco.nationsapp.model.tasks.BasicCountryEntry;
import com.qualco.nationsapp.model.tasks.CountryWithMaxGDPPerCapitaEntry;
import com.qualco.nationsapp.model.tasks.StatsEntry;
import com.qualco.nationsapp.persistence.DBConnection;
import com.qualco.nationsapp.util.PaginatedQueryParams;
import com.qualco.nationsapp.util.logger.Logged;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Logged
public class NationsRestService {

    private final DBConnection dbConnection;

    public List<BasicCountryEntry> getAllCountries(PaginatedQueryParams params){
        return dbConnection.getBasicCountryInfo(params);
    }

    public List<String> getLanguagesOfCountry(String country){
        return dbConnection.getLanguagesOfCountry(country);
    }

    public List<CountryWithMaxGDPPerCapitaEntry> getMaxGDPPerCapita(PaginatedQueryParams params){
        return dbConnection.getMaxGDPPerCapita(params);
    }

    public List<StatsEntry> getStats(PaginatedQueryParams params){
        return dbConnection.getStats(params);
    }
}
