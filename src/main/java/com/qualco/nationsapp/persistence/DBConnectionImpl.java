package com.qualco.nationsapp.persistence;

import com.qualco.nationsapp.model.BasicCountryEntry;
import com.qualco.nationsapp.model.CountryWithMaxGDPPerCapitaEntry;
import com.qualco.nationsapp.model.StatsEntry;
import com.qualco.nationsapp.util.PaginatedQueryParams;
import com.qualco.nationsapp.util.exception.DataAccessLayerException;
import com.qualco.nationsapp.util.logger.Logged;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.qualco.nationsapp.util.Constants.YEAR_FROM;
import static com.qualco.nationsapp.util.Constants.YEAR_TO;

@Logged
@Slf4j
@Repository
@RequiredArgsConstructor
public class DBConnectionImpl implements DBConnection{

    private static final String IDENTITY = "1 = 1";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BasicCountryEntry> getBasicCountryInfo(PaginatedQueryParams params) {
        String query = String.format(
                """
                SELECT countries.name AS 'name', countries.area AS 'area', countries.country_code2 AS 'countryCode'
                FROM countries ORDER BY %s LIMIT %s OFFSET %s;
                """,  params.getSortByField() + " " + params.getSortOrder().toString(),
                params.getPageSize(), params.getPage() * params.getPageSize());
        try {
            return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BasicCountryEntry.class));
        } catch (EmptyResultDataAccessException exception){
            log.warn("Empty result encountered.");
            return Collections.emptyList();
        } catch(DataAccessException exception){
            log.warn("An exception occurred, with message: " + exception.getMessage());
            throw new DataAccessLayerException();
        }
    }

    @Override
    public List<String> getLanguagesOfCountry(String country) {
        String query = String.format(
                """
                SELECT languages.language AS language 
                FROM languages INNER JOIN country_languages ON languages.language_id =  country_languages.language_id 
                INNER JOIN countries ON country_languages.country_id = countries.country_id AND countries.name = %s
                """, country);
        try {
            return jdbcTemplate.queryForList(query, String.class);
        } catch (EmptyResultDataAccessException exception) {
            log.warn("Empty result encountered.");
            return Collections.emptyList();
        } catch(DataAccessException exception){
            log.warn("An exception occurred, with message: " + exception.getMessage());
            throw new DataAccessLayerException();
        }
    }

    @Override
    public List<CountryWithMaxGDPPerCapitaEntry> getMaxGDPPerCapita(PaginatedQueryParams params) {
        String query = String.format(
                """
                SELECT countries.name AS name, countries.country_code3 AS countryCode, max(gdp/population) AS maxGDPPerCapita
                FROM countries INNER JOIN country_stats ON countries.country_id = country_stats.country_id GROUP BY country_stats.country_id
                ORDER BY %s LIMIT %s OFFSET %s;
                """, params.getSortByField() + " " + params.getSortOrder().toString(), params.getPageSize(), params.getPage() * params.getPageSize());
        try {
            return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(CountryWithMaxGDPPerCapitaEntry.class));
        } catch(EmptyResultDataAccessException exception){
            log.warn("Empty result encountered.");
            return Collections.emptyList();
        } catch(DataAccessException exception){
            log.warn("An exception occurred, with message: " + exception.getMessage());
            throw new DataAccessLayerException();
        }
    }

    @Override
    public List<StatsEntry> getStats(PaginatedQueryParams params) {
        String query = String.format(
                """
                SELECT continents.name AS continent_name, regions.name AS region_name, countries.name AS country_name,
                country_stats.year AS year, country_stats.population AS population, country_stats.gdp AS gdp
                FROM continents INNER JOIN regions ON continents.continent_id = regions.continent_id INNER JOIN countries ON
                regions.region_id = countries.region_id INNER JOIN country_stats ON countries.country_id = country_stats.country_id
                WHERE %s ORDER BY %s LIMIT %s OFFSET %s
                """, buildWhereClause(params.getFilterParams()), params.getSortByField() + " " + params.getSortOrder().toString(),
                params.getPageSize(), params.getPage() * params.getPageSize());
        try {
            return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(StatsEntry.class));
        } catch (EmptyResultDataAccessException exception){
            log.warn("Empty result encountered.");
            return Collections.emptyList();
        } catch(DataAccessException exception){
            log.warn("An exception occurred, with message: " + exception.getMessage());
            throw new DataAccessLayerException();
        }
    }

    private String buildWhereClause(Map<String, String> filters){
        if(filters.containsKey(YEAR_FROM) && filters.containsKey(YEAR_TO)){
            return "year >= " + filters.get(YEAR_FROM) + " AND year <="  + filters.get(YEAR_TO);
        } else if(filters.containsKey(YEAR_FROM)){
            return "year >= " + filters.get(YEAR_FROM);
        } else if(filters.containsKey(YEAR_TO)){
            return "year <= " + filters.get(YEAR_TO);
        }
        return IDENTITY;
    }
}
