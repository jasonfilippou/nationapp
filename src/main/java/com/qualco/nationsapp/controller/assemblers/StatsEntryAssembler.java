package com.qualco.nationsapp.controller.assemblers;

import static com.qualco.nationsapp.util.Constants.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.qualco.nationsapp.controller.NationsRestController;
import com.qualco.nationsapp.model.tasks.StatsEntry;
import com.qualco.nationsapp.util.SortOrder;

import lombok.NonNull;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * A {@link RepresentationModelAssembler} that formats {@link StatsEntry} entities for presenting a link to
 * relevant methods of {@link NationsRestController}.
 *
 * @author jason
 */
@Component
public class StatsEntryAssembler implements RepresentationModelAssembler<StatsEntry, EntityModel<StatsEntry>> {

    @Override
    public @NonNull EntityModel<StatsEntry> toModel(@NonNull StatsEntry entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(NationsRestController.class).getStats(Integer.parseInt(DEFAULT_PAGE_IDX), Integer.parseInt(DEFAULT_PAGE_SIZE),
                        "continent_name", SortOrder.valueOf(DEFAULT_SORT_ORDER), entity.getRegionName())).withRel("Stats of Region"));
    }
}
