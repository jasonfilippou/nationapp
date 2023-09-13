package com.qualco.nationsapp.util;


import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public final class Constants {
    private Constants(){}

    public static final String DEFAULT_PAGE_IDX = "0";
    public static final String DEFAULT_PAGE_SIZE = "5";
    public static final String DEFAULT_SORT_BY_FIELD = "id";
    public static final String DEFAULT_SORT_ORDER = "ASC";

    public static final String GLOBAL_YEAR_PATTERN = "yyyy";
    public static final DateTimeFormatter YEAR_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern(GLOBAL_YEAR_PATTERN)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    public static final String YEAR_FROM = "year_from";

    public static final String YEAR_TO = "year_to";
}
