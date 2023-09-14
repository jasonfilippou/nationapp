package com.qualco.nationsapp.controller.assemblers;

import com.qualco.nationsapp.controller.NationsRestController;
import com.qualco.nationsapp.model.tasks.BasicCountryEntry;
import com.qualco.nationsapp.util.SortOrder;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static com.qualco.nationsapp.util.Constants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A {@link RepresentationModelAssembler} that formats {@link BasicCountryEntry} entities for presenting a link to
 * relevant methods of {@link NationsRestController}.
 *
 * @author jason
 */
@Component
public class BasicCountryEntryAssembler implements RepresentationModelAssembler<BasicCountryEntry, EntityModel<BasicCountryEntry>> {

    @Override
    public @NonNull EntityModel<BasicCountryEntry> toModel(@NonNull BasicCountryEntry entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(NationsRestController.class).getAllCountries(Integer.parseInt(DEFAULT_PAGE_IDX),
                        Integer.parseInt(DEFAULT_PAGE_SIZE), "name", SortOrder.valueOf(DEFAULT_SORT_ORDER)))
                        .withSelfRel());
    }
}
