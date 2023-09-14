package com.qualco.nationsapp.persistence;

import com.qualco.nationsapp.model.tasks.BasicCountryEntry;
import com.qualco.nationsapp.model.tasks.MaxGDPPerCapitaEntry;
import com.qualco.nationsapp.model.tasks.StatsEntry;
import com.qualco.nationsapp.util.PaginatedQueryParams;
import com.qualco.nationsapp.util.exception.DataAccessLayerException;

import java.util.List;

/**
 *  An interface that defines the operations that we want from the persistence layer of our application.
 *
 * @author jason
 */
public interface DBConnection {

    /**
     * Return a {@link List} of {@link BasicCountryEntry} instances that answer task 1(a) of the instructions.
     * @param params An instance of {@link PaginatedQueryParams} which encapsulates the sorting and pagination properties
     *               set by our query.
     * @return A {@link List} of {@link BasicCountryEntry} instances, one per row that the underlying {@code SELECT} query
     * returns.
     * @throws DataAccessLayerException if an unrecoverable error occurs at the database level.
     */
    List<BasicCountryEntry> getBasicCountryInfo(PaginatedQueryParams params) throws DataAccessLayerException;

    /**
     * Return a {@link List} of {@link String}s, one for each language spoken in the country provided (according to our data).
     * This answers task 1(b) of the instructions.
     * @param country The country to search the spoken languages of.
     * @return A {@link List} of {@link String}s, each corresponding to a language spoken in the country provided.
     * @throws DataAccessLayerException if an unrecoverable error occurs at the database level.
     */
    List<String> getLanguagesOfCountry(String country) throws DataAccessLayerException;

    /**
     * Answer task 2 of the instructions, returning the country name, country_code3 and max GDP per capita for all the
     * years of observations that we have for all countries.
     * @param params An instance of {@link PaginatedQueryParams} which encapsulates the sorting and pagination properties
     *               set by our query.
     * @return A {@link List} of {@link MaxGDPPerCapitaEntry} instances, one per row returned by the underlying {@code SELECT}
     * query.
     * @throws DataAccessLayerException if an unrecoverable error occurs at the database level.
     */
    List<MaxGDPPerCapitaEntry> getMaxGDPPerCapita(PaginatedQueryParams params) throws DataAccessLayerException;

    /**
     * Answer task 3 of the instructions, returning the continent name, region name, country_name, year, population, gdp for all
     * countries in our database.
     * @param params An instance of {@link PaginatedQueryParams} which encapsulates the sorting and pagination properties
     *               set by our query.
     * @return Α {@link List} of {@link StatsEntry} instances, one per row returned by the underlying {@code SELECT}
     * statement.
     * @throws DataAccessLayerException if an unrecoverable error occurs at the database level.
     */
    List<StatsEntry> getStats(PaginatedQueryParams params) throws DataAccessLayerException;

}
