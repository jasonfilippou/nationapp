package com.qualco.nationsapp.util;


import java.time.format.DateTimeFormatter;

public final class Constants {
    private Constants(){}

    public static final String DEFAULT_PAGE_IDX = "0";
    public static final String DEFAULT_PAGE_SIZE = "5";
    public static final String DEFAULT_SORT_BY_FIELD = "id";
    public static final String DEFAULT_SORT_ORDER = "ASC";

    public static final String GLOBAL_YEAR_PATTERN = "yyyy";

    public static final DateTimeFormatter YEAR_FORMATTER =
            DateTimeFormatter.ofPattern(GLOBAL_YEAR_PATTERN);
}
