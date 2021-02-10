package com.embl.fire.api;

import com.embl.fire.entities.Person;
import com.embl.fire.mapper.PersonMapper;
import com.embl.fire.mapper.PersonPatchMapper;
import com.embl.fire.model.PersonDTO;
import com.embl.fire.model.PersonList;
import com.embl.fire.model.PersonPatchDTO;
import com.embl.fire.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonsApiDelegateImpl implements PersonsApiDelegate {

    private final PersonService personService;
    private final PersonMapper personMapper;
    private final PersonPatchMapper personPatchMapper;

    @Autowired
    public PersonsApiDelegateImpl(PersonService personService, PersonMapper personMapper,
                                  PersonPatchMapper personPatchMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
        this.personPatchMapper = personPatchMapper;
    }

    @Override
    public ResponseEntity<PersonList> getAllPersonEntities() {
        List<PersonDTO> personDTOList = personService.findAllPersons().stream()
                .map(personMapper::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new PersonList().person(personDTOList), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PersonDTO> getPersonEntityByFirstName(String firstName) {
        Person person = personService.findPersonByFirstName(firstName);
        return new ResponseEntity<>(personMapper.toDTO(person), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PersonDTO> savePersonEntity(PersonDTO personDTO) {
        Person person = personService.savePerson(personMapper.toEntity(personDTO));
        return new ResponseEntity<>(personMapper.toDTO(person), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePersonEntityByFirstName(String firstName) {
        personService.deletePersonByFirstName(firstName);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PersonDTO> updatePersonEntityByFirstName(
            String firstName, PersonPatchDTO personPatchDTO) {
        Person person = personService.patchPerson(firstName, personPatchMapper.toEntity(personPatchDTO));
        return new ResponseEntity<>(personMapper.toDTO(person), HttpStatus.OK);
    }
}
