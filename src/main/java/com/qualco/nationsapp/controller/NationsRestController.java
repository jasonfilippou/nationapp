package com.qualco.nationsapp.controller;

import com.qualco.nationsapp.exception.BadDateFormatException;
import com.qualco.nationsapp.exception.InvalidSortByFieldException;
import com.qualco.nationsapp.util.PaginatedQueryParams;
import com.qualco.nationsapp.util.SortOrder;
import com.qualco.nationsapp.util.logger.Logged;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.qualco.nationsapp.util.Constants.*;

@RestController
@RequestMapping("/nationapi")
@RequiredArgsConstructor
@Logged
@Validated
@Tag(name = "2. Nations REST Endpoints")
public class NationsRestController {

    private final NationsRestService service;

    // Task 1(a)
    @GetMapping("/countries")
    public ResponseEntity<?> getAllCountries(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "name") @NonNull @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder)
            throws InvalidSortByFieldException {
        sortByField = sortByField.trim();
        List<String> acceptableFieldsForSorting = List.of("name", "area", "country_code2");
        checkIfFieldToSortByIsAcceptable(sortByField, acceptableFieldsForSorting);
        return service.getAllCountries(page, size, sortByField, sortOrder);
    }

    private void checkIfFieldToSortByIsAcceptable(String field, List<String> acceptableFields)
            throws InvalidSortByFieldException {
        if(acceptableFields.stream().noneMatch(field::equals)){
            throw new InvalidSortByFieldException(field, acceptableFields);
        }
    }

    // Task 1(b)
    @GetMapping("/languages/{countryName}")
    public ResponseEntity<?> getLanguagesOfCountry(@PathVariable @NotBlank String countryName){
        return service.getLanguagesOfCountry(countryName);
    }

    // Task 2
    @GetMapping("/maxgdppercapita/")
    public ResponseEntity<?> getMaxGdpPerCapita(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "name") @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder){
        sortByField = sortByField.trim();
        checkIfFieldToSortByIsAcceptable(sortByField, Collections.singletonList("name"));
        return service.getMaxGDPPerCapita(PaginatedQueryParams.builder()
                .page(page)
                .pageSize(size)
                .sortByField(sortByField)
                .sortOrder(sortOrder)
                .build());

    }

    // Task 3(a)
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_IDX) @Min(0) Integer page,
            @RequestParam(name = "items_in_page", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer size,
            @RequestParam(name = "sort_by_field", defaultValue = "name") @NotBlank String sortByField,
            @RequestParam(name = "sort_order", defaultValue = DEFAULT_SORT_ORDER) @NonNull SortOrder sortOrder,
            @RequestParam(name = "yearFrom", defaultValue = "1960") @NotBlank String yearFrom,
            @RequestParam(name = "yearTo", defaultValue = "2018") @NotBlank String yearTo)
            throws InvalidSortByFieldException, BadDateFormatException{
        sortByField = sortByField.trim();
        checkIfFieldToSortByIsAcceptable(sortByField, List.of("continent_name", "region_name", "country_name", "year",
                "population", "gdp"));
        checkYearFormat(yearFrom);
        checkYearFormat(yearTo);
        return service.getStats(PaginatedQueryParams.builder()
                .page(page)
                .sortByField(sortByField)
                .pageSize(size)
                .sortOrder(sortOrder)
                .filterParams(Map.of("yearFrom", yearFrom, "yearTo", yearTo))
                .build());
    }

    private void checkYearFormat(String year) throws BadDateFormatException{
        try {
            LocalDate.parse(year, YEAR_FORMATTER);
        } catch(DateTimeParseException exception){
            throw new BadDateFormatException("year " + year + "not in yyyy format.");
        }
    }

    // Task 3(b)
}
