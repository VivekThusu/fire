package com.embl.fire.mapper;

import com.embl.fire.entities.PersonPatch;
import com.embl.fire.model.PersonPatchDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonPatchMapper {

    PersonPatchDTO toDTO(PersonPatch personPatch);

    PersonPatch toEntity(PersonPatchDTO personPatchDTO);
}
