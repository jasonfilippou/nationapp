package com.qualco.nationsapp.service;

import com.qualco.nationsapp.model.tasks.BasicCountryEntry;
import com.qualco.nationsapp.model.tasks.MaxGDPPerCapitaEntry;
import com.qualco.nationsapp.model.tasks.StatsEntry;
import com.qualco.nationsapp.persistence.DBConnection;
import com.qualco.nationsapp.util.PaginatedQueryParams;
import com.qualco.nationsapp.util.logger.Logged;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-level class that connects the controller level to the persistence store.
 *
 * @author jason
 */
@Service
@RequiredArgsConstructor
@Logged
public class NationsRestService {

    /* This service class is currently a simple pass-through to the persistence layer, but
        this could very much change in the future.
     */
    private final DBConnection dbConnection;

    /**
     * Answers task 1(a) of the provided instructions.
     * @param params An instance of {@link PaginatedQueryParams} which encapsulates the sorting and pagination properties
     *               set by our query.
     * @return A {@link List} of {@link BasicCountryEntry} instances.
     */
    public List<BasicCountryEntry> getAllCountries(PaginatedQueryParams params){
        return dbConnection.getBasicCountryInfo(params);
    }

    /**
     * Answers task 1(b) of the provided instructions.
     * @param country The country to return the spoken languages of.
     * @return A {@link List} of {@link String}s, one per spoken language in the provided country.
     */
    public List<String> getLanguagesOfCountry(String country){
        return dbConnection.getLanguagesOfCountry(country);
    }

    /**
     * Answers task (2) of the provided instructions.
     * @param params An instance of {@link PaginatedQueryParams} which encapsulates the sorting and pagination properties
     *               set by our query.
     * @return A {@link List} of {@link MaxGDPPerCapitaEntry} instances.
     */
    public List<MaxGDPPerCapitaEntry> getMaxGDPPerCapita(PaginatedQueryParams params){
        return dbConnection.getMaxGDPPerCapita(params);
    }

    /**
     * Answers task (3) of the provided instructions.
     * @param params An instance of {@link PaginatedQueryParams} which encapsulates the sorting and pagination properties
     *               set by our query.
     * @return A {@link List} of {@link StatsEntry} instances.
     */
    public List<StatsEntry> getStats(PaginatedQueryParams params){
        return dbConnection.getStats(params);
    }
}
