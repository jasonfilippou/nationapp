package com.qualco.nationsapp.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.function.Predicate;

/**
 * A container for parameters used in pagination / sorting / filter aggregate GET queries.
 * 
 * @author jason 
 */
@Builder
@Getter
@ToString
public class PaginatedQueryParams {
  private Integer page;
  private Integer pageSize;
  private String sortByField;
  private SortOrder sortOrder;
  private Map<String, String> filterParams;
  private Predicate<?> predicate;
}
