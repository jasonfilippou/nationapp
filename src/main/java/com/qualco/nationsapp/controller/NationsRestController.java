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

    // Task 1(a)

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

    // Task 1(b)
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

    // Task 2

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
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder){
        sortByField = sortByField.trim();
        checkIfFieldToSortByIsAcceptable(sortByField, List.of("name", "country_code3", "maxGDPPerCapita"));
        return ResponseEntity.ok(service.getMaxGDPPerCapita(PaginatedQueryParams.builder()
                .page(page)
                .pageSize(size)
                .sortByField(sortByField)
                .sortOrder(sortOrder)
                .build()).stream().map(maxGDPPerCapitaEntryAssembler::toModel).collect(Collectors.toList()));
    }

    // Task 3(a) and 3(b)
    @Operation(summary = "Get continent, region, country, year, population and gdp for all countries.")
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
                    @ApiResponse(
                            responseCode = "500",
                            description = "System error",
                            content = @Content)
            })
    @GetMapping("/stats")
    public ResponseEntity<List<EntityModel<StatsEntry>>> getStats(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "continent_name") @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder,
            @RequestParam(name = "year_from", required = false) String yearFrom,
            @RequestParam(name = "year_to", required = false) String yearTo,
            @RequestParam(name = "region", required = false) String region)
            throws InvalidSortByFieldException, BadDateFormatException, ValidationException{
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

    // 3(a) and 3(b) with input region.

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
