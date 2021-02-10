package com.embl.fire.mapper;

import com.embl.fire.entities.Person;
import com.embl.fire.model.PersonDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDTO toDTO(Person person);

    Person toEntity(PersonDTO personDTO);
}
