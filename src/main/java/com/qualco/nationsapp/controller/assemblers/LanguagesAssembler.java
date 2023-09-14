package com.qualco.nationsapp.controller.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.qualco.nationsapp.controller.NationsRestController;
import com.qualco.nationsapp.model.tasks.CountryAndLanguageEntry;

import lombok.NonNull;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * A {@link RepresentationModelAssembler} that formats {@link CountryAndLanguageEntry} entities for presenting a link to
 * relevant methods of {@link NationsRestController}.
 *
 * @author jason
 */
@Component
public class LanguagesAssembler implements RepresentationModelAssembler<CountryAndLanguageEntry, EntityModel<CountryAndLanguageEntry>> {

    @Override
    public @NonNull EntityModel<CountryAndLanguageEntry> toModel(@NonNull CountryAndLanguageEntry entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(NationsRestController.class).getLanguagesOfCountry(entity.getCountry())).withSelfRel());
    }
}
