package com.qualco.nationsapp.util;


import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * Various constants used by our application.
 *
 * @author jason
 */
public final class Constants {
    private Constants(){}

    /**
     * Standard authentication header prefix.
     */
    public static final String AUTH_HEADER_BEARER_PREFIX = "Bearer" + " ";
    /** Tune this to affect how long the JWT token lasts. Default is 5 * 60 * 60, for 5 hours. */
    public static final long JWT_VALIDITY = 5 * 60 * 60;

    public static final String DEFAULT_PAGE_IDX = "0";
    public static final String DEFAULT_PAGE_SIZE = "5";
    public static final String DEFAULT_SORT_BY_FIELD = "id";
    public static final String DEFAULT_SORT_ORDER = "ASC";

    public static final String GLOBAL_YEAR_PATTERN = "yyyy";

    /**
     * A global {@link DateTimeFormatter} instance used to validate inputted years in filters of
     * {@link com.qualco.nationsapp.controller.NationsRestController} methods.
     *
     * @see com.qualco.nationsapp.controller.NationsRestController#getStats(Integer, Integer, String, SortOrder, String, String, String)
     */
    public static final DateTimeFormatter YEAR_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern(GLOBAL_YEAR_PATTERN)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    public static final String YEAR_FROM = "year_from";

    public static final String YEAR_TO = "year_to";
}
