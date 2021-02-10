package com.embl.fire.services;

import com.embl.fire.entities.Person;
import com.embl.fire.entities.PersonPatch;

import java.util.List;

public interface PersonService {

    List<Person> findAllPersons();

    Person findPersonByFirstName(String firstName);

    Person savePerson(Person person);

    void deletePersonByFirstName(String firstName);

    Person patchPerson(String firstName, PersonPatch personPatch);

}
