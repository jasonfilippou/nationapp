package com.qualco.nationsapp.controller;

import static com.qualco.nationsapp.util.Constants.*;

import com.google.common.collect.Maps;
import com.qualco.nationsapp.controller.assemblers.BasicCountryEntryAssembler;
import com.qualco.nationsapp.controller.assemblers.LanguagesAssembler;
import com.qualco.nationsapp.controller.assemblers.MaxGDPPerCapitaEntryAssembler;
import com.qualco.nationsapp.controller.assemblers.StatsEntryAssembler;
import com.qualco.nationsapp.model.tasks.BasicCountryEntry;
import com.qualco.nationsapp.model.tasks.CountryAndLanguageEntry;
import com.qualco.nationsapp.model.tasks.MaxGDPPerCapitaEntry;
import com.qualco.nationsapp.model.tasks.StatsEntry;
import com.qualco.nationsapp.service.NationsRestService;
import com.qualco.nationsapp.util.PaginatedQueryParams;
import com.qualco.nationsapp.util.SortOrder;
import com.qualco.nationsapp.util.exception.BadDateFormatException;
import com.qualco.nationsapp.util.exception.CountryNotFoundException;
import com.qualco.nationsapp.util.exception.InvalidSortByFieldException;
import com.qualco.nationsapp.util.logger.Logged;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link RestController} that exposes endpoints that enable the functionality required by the assignment's tasks.
 *
 * @author jason
 */
@RestController
@RequestMapping("/nationapi")
@RequiredArgsConstructor
@Logged
@Validated
@Tag(name = "2. Nations REST Endpoints")
public class NationsRestController {

    private final NationsRestService service;
    private final StatsEntryAssembler statsEntryAssembler;
    private final MaxGDPPerCapitaEntryAssembler maxGDPPerCapitaEntryAssembler;
    private final LanguagesAssembler languagesAssembler;
    private final BasicCountryEntryAssembler basicCountryEntryAssembler;


    /**
     * GET endpoint that answers task 1(a): An endpoint that returns the name, area, country_code2 for all countries.
     * Implemented with pagination and sorting.
     * @param page The page of data to return.
     * @param size The number of entries per page.
     * @param sortByField The field to sort the results by. Supported: &quot;name&quot;, &quot;area&quot;, &quot;country_code2&quot;
     * @param sortOrder The ordering of the sort required: ASC or DESC.
     * @return A {@link ResponseEntity} with HTTP Status 200 OK and a list of {@link EntityModel} wrappers over
     * {@link BasicCountryEntry} instances, one per country, or a different status code and helpful message.
     * @throws InvalidSortByFieldException If the field to sort by is not one of &quot;name&quot;, &quot;area&quot;, &quot;country_code2&quot;.
     * @throws ConstraintViolationException If there is some other violation of constraints for sorting / pagination parameters.
     */
    @Operation(summary = "Return name, area and country_code2 for all countries")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Data successfully returned",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad sorting / pagination parameters provided",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthenticated user",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "System error",
                            content = @Content)
            })
    @GetMapping("/countries")
    public ResponseEntity<List<EntityModel<BasicCountryEntry>>> getAllCountries(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "name") @NonNull @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder)
            throws InvalidSortByFieldException {
        sortByField = sortByField.trim();
        List<String> acceptableFieldsForSorting = List.of("name", "area", "country_code2");
        checkIfFieldToSortByIsAcceptable(sortByField, acceptableFieldsForSorting);
        return ResponseEntity.ok(service.getAllCountries(PaginatedQueryParams.builder()
                .page(page)
                .pageSize(size)
                .sortByField(sortByField)
                .sortOrder(sortOrder)
                .build()).stream().map(basicCountryEntryAssembler::toModel).collect(Collectors.toList()));
    }

    private void checkIfFieldToSortByIsAcceptable(String field, List<String> acceptableFields)
            throws InvalidSortByFieldException {
        if(acceptableFields.stream().noneMatch(field::equals)){
            throw new InvalidSortByFieldException(field, acceptableFields);
        }
    }

    /**
     * GET endpoint that enables task 1(b): getting all the spoken languages of a given country. Because of the small average
     * number of languages per country, we do not implement this query using pagination and sorting.
     * @param countryName The country to find the spoken languages of.
     * @return A {@link ResponseEntity} with HTTP status 200 OK and a list of {@link EntityModel} wrappers over
     * {@link CountryAndLanguageEntry} instances, one per language spoken in the provided country.
     * @throws CountryNotFoundException If the country provided was not found in our database.
     */
    @Operation(summary = "Get languages of provided country")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Data successfully returned",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Empty country string provided",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthenticated user",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Country not found",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "System error",
                            content = @Content)
            })
    @GetMapping("/languages/{countryName}")
    public ResponseEntity<List<EntityModel<CountryAndLanguageEntry>>> getLanguagesOfCountry(@PathVariable @NotBlank String countryName)
                throws CountryNotFoundException {
        List<String> languages = service.getLanguagesOfCountry(countryName);
        if(languages.isEmpty()){
            throw new CountryNotFoundException(countryName);
        }
        return ResponseEntity.ok(languages.stream().map(language->
                languagesAssembler.toModel(new CountryAndLanguageEntry(countryName, language))).collect(Collectors.toList()));
    }


    /**
     * GET Endpoint that implements task 2 of the instructions; getting the name, country_code3 and maximum GDP for
     * all countries across all years of data.
     * @param page The page of data to return.
     * @param size The number of entries per page.
     * @param sortByField The field to sort the results by. Supported: &quot;name&quot;, &quot;country_code3&quot;, &quot;maxGDPPerCapita&quot;
     * @param sortOrder The ordering of the sort required: ASC or DESC.
     * @return A {@link ResponseEntity} over with HTTP Status 200 OK and a {@link List} of {@link EntityModel} wrappers
     * over {@link MaxGDPPerCapitaEntry} instances, one per country, or a different status code and a helpful message in the
     * case of an unsuccessful request.
     * @throws InvalidSortByFieldException if the field to sort by provided was not one of &quot;name&quot;, &quot;country_code3&quot;, &quot;maxGDPPerCapita&quot;
     * @throws ConstraintViolationException If there is some other violation of constraints for sorting / pagination parameters.
      */
    @Operation(summary = "Get name, country_code3 and maximum GDP per capita for all countries across all years of data.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Data successfully returned",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad sorting / pagination parameters provided",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthenticated user",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "System error",
                            content = @Content)
            })
    @GetMapping("/maxgdppercapita")
    public ResponseEntity<List<EntityModel<MaxGDPPerCapitaEntry>>> getMaxGdpPerCapita(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "name") @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder)
        throws InvalidSortByFieldException, ConstraintViolationException{
        sortByField = sortByField.trim();
        checkIfFieldToSortByIsAcceptable(sortByField, List.of("name", "country_code3", "maxGDPPerCapita"));
        return ResponseEntity.ok(service.getMaxGDPPerCapita(PaginatedQueryParams.builder()
                .page(page)
                .pageSize(size)
                .sortByField(sortByField)
                .sortOrder(sortOrder)
                .build()).stream().map(maxGDPPerCapitaEntryAssembler::toModel).collect(Collectors.toList()));
    }

    /**
     * GET endpoint that implements task 3 of the instructions; get the continent, region, country, year, population
     * and gdp of all countries, optionally applying filters for the years or the region. If the filters result in an empty
     * response, we still return HTTP 200 OK and the empty response.
     * @param page The page of data to return.
     * @param size The number of entries per page.
     * @param sortByField The field to sort the results by. Supported: &quot;continent_name&quot;, &quot;region_name&quot;,
     *                   &quot;country_name&quot;, &quot;year&quot;, &quot;population&quot;, &quot;gdp&quot;.
     * @param sortOrder The ordering of the sort required: ASC or DESC.
     * @param yearFrom Optional parameter. The first year to include results of.
     * @param yearTo Optional parameter. The last year to include results of.
     * @param region Optional parameter. The region to limit our results to.
     * @return Α {@link ResponseEntity} with HTTP Status 200 OK and a {@link List} of {@link EntityModel} wrappers over
     * {@link StatsEntry} instances, one per country that satisfies the filters, if any, or a different status code and
     * helpful message.
     * @throws InvalidSortByFieldException If the field to sort by is not one of &quot;continent_name&quot;, &quot;region_name&quot;,
     *                   &quot;country_name&quot;, &quot;year&quot;, &quot;population&quot;, or &quot;gdp&quot;
     * @throws BadDateFormatException If either one of the years specified is not in YYYY format.
     * @throws ValidationException If the &quot; region &quot; parameter is provided, but it maps to an empty string.
     * @throws ConstraintViolationException If there is some other violation of constraints for sorting / pagination parameters.
     */
    @Operation(
            summary = "Get continent, region, country, year, population and gdp for all countries.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Data successfully returned",
                        content = @Content),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad sorting / pagination / filter parameters provided",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthenticated user",
                        content = @Content),
                @ApiResponse(responseCode = "500", description = "System error", content = @Content)
            })
    @GetMapping("/stats")
    public ResponseEntity<List<EntityModel<StatsEntry>>> getStats(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1)
                    Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "continent_name") @NotBlank
                    String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull
                    SortOrder sortOrder,
            @RequestParam(name = "year_from", required = false) String yearFrom,
            @RequestParam(name = "year_to", required = false) String yearTo,
            @RequestParam(name = "region", required = false) String region)
            throws InvalidSortByFieldException, BadDateFormatException, ValidationException {
        sortByField = sortByField.trim();
        checkIfFieldToSortByIsAcceptable(sortByField, List.of("continent_name", "region_name", "country_name", "year",
                "population", "gdp"));
        checkFilters(yearFrom, yearTo, region);
        return ResponseEntity.ok(service.getStats(PaginatedQueryParams.builder()
                .page(page)
                .sortByField(sortByField)
                .pageSize(size)
                .sortOrder(sortOrder)
                .filterParams(createFilterParams(yearFrom, yearTo, region))
                .build()).stream().map(statsEntryAssembler::toModel).collect(Collectors.toList()));
    }

    private void checkFilters(String yearFrom, String yearTo, String region) throws BadDateFormatException, ValidationException{
        if(yearFrom != null){
            checkYearFormat(yearFrom);
        }
        if(yearTo != null){
            checkYearFormat(yearTo);
        }
        if (region != null && StringUtils.isBlank(region)) {
            throw new ValidationException("When provided, \"region\" key needs to map to non-empty value.");
        }
    }

    private void checkYearFormat(String year) throws BadDateFormatException{
        try {
            LocalDate.parse(year, YEAR_FORMATTER); // Call only for side-effect
        } catch(DateTimeParseException exception){
            throw new BadDateFormatException("year " + year + " not in yyyy format.");
        }
    }

    private Map<String, String> createFilterParams(String yearFrom, String yearTo, String region){
        Map<String, String> filterParams = Maps.newHashMap();
        if(yearFrom != null){
            filterParams.put(YEAR_FROM, yearFrom);
        }
        if(yearTo != null){
            filterParams.put(YEAR_TO, yearTo);
        }
        if(region != null){
            filterParams.put("region", region);
        }
        return filterParams;
    }

    /**
     * GET endpoint similar to {@link #getStats(Integer, Integer, String, SortOrder, String, String, String)} that takes the region as a
     * path variable. It is mostly useful for {@link StatsEntryAssembler} to provide a useful link in the {@link ResponseEntity}
     * {@literal link} field for that endpoint.
     * @param page The page of data to return.
     * @param size The number of entries per page.
     * @param sortByField The field to sort the results by. Supported: &quot;continent_name&quot;, &quot;region_name&quot;,
     *                   &quot;country_name&quot;, &quot;year&quot;, &quot;population&quot;, &quot;gdp&quot;.
     * @param sortOrder The ordering of the sort required: ASC or DESC.
     * @param region The region to get data of.
     * @return Α {@link ResponseEntity} with HTTP Status 200 OK and a {@link List} of {@link EntityModel} wrappers over
     * {@link StatsEntry} instances, one per country that satisfies the filters, if any, or a different status code and helpful message.
     * @throws InvalidSortByFieldException If the field to sort by is not one of &quot;continent_name&quot;, &quot;region_name&quot;,
     *                   &quot;country_name&quot;, &quot;year&quot;, &quot;population&quot;, or &quot;gdp&quot;
     * @throws BadDateFormatException If either one of the years specified is not in YYYY format.
     * @throws ValidationException If the &quot; region &quot; parameter is provided, but it maps to an empty string.
     * @throws ConstraintViolationException If there is some other violation of constraints for sorting / pagination parameters.
     */
    @Operation(summary = "Get continent, country, year, population and gdp for all countries of provided region.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Data successfully returned",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad sorting / pagination / region parameters provided",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthenticated user",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "System error",
                            content = @Content)
            })
    @GetMapping("/stats/{region}")
    public ResponseEntity<List<EntityModel<StatsEntry>>> getStats(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "continent_name") @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder,
            @PathVariable @NotBlank String region)
            throws InvalidSortByFieldException{
        sortByField = sortByField.trim();
        checkIfFieldToSortByIsAcceptable(sortByField, List.of("continent_name", "region_name", "country_name", "year",
                "population", "gdp"));
        return ResponseEntity.ok(service.getStats(PaginatedQueryParams.builder()
                .page(page)
                .sortByField(sortByField)
                .pageSize(size)
                .sortOrder(sortOrder)
                .filterParams(Map.of("region", region))
                .build()).stream().map(statsEntryAssembler::toModel).collect(Collectors.toList()));
    }
}
