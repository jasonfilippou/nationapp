package com.qualco.nationsapp.controller.assemblers;

import static com.qualco.nationsapp.util.Constants.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.qualco.nationsapp.controller.NationsRestController;
import com.qualco.nationsapp.model.tasks.MaxGDPPerCapitaEntry;
import com.qualco.nationsapp.util.SortOrder;

import lombok.NonNull;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * A {@link RepresentationModelAssembler} that formats {@link MaxGDPPerCapitaEntry} entities for presenting a link to
 * relevant methods of {@link NationsRestController}.
 *
 * @author jason
 */
@Component
public class MaxGDPPerCapitaEntryAssembler implements RepresentationModelAssembler<MaxGDPPerCapitaEntry, EntityModel<MaxGDPPerCapitaEntry>> {

    @Override
    public @NonNull EntityModel<MaxGDPPerCapitaEntry> toModel(@NonNull MaxGDPPerCapitaEntry entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(NationsRestController.class).getMaxGdpPerCapita(Integer.parseInt(DEFAULT_PAGE_IDX),
                        Integer.parseInt(DEFAULT_PAGE_SIZE), "name", SortOrder.valueOf(DEFAULT_SORT_ORDER))).withSelfRel());
    }
}
