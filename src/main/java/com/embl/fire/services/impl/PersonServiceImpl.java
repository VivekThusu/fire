package com.embl.fire.services.impl;

import com.embl.fire.entities.Person;
import com.embl.fire.entities.PersonPatch;
import com.embl.fire.exceptions.EntityNotFoundException;
import com.embl.fire.repositories.PersonRepository;
import com.embl.fire.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person findPersonByFirstName(String firstName) {
        return personRepository.findById(firstName)
                .orElseThrow(() -> new EntityNotFoundException(firstName));
    }

    @Override
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void deletePersonByFirstName(String firstName) {
        Person person = personRepository.findById(firstName)
                .orElseThrow(() -> new EntityNotFoundException(firstName));
        personRepository.delete(person);
    }

    @Override
    public Person patchPerson(String firstName, PersonPatch personPatch) {
        Person person = personRepository.findById(firstName)
                .orElseThrow(() -> new EntityNotFoundException(firstName));
        person = personPatch.applyPatch(person);
        return personRepository.save(person);
    }
}
