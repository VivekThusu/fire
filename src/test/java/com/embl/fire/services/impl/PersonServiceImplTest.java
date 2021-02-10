package com.embl.fire.services.impl;

import com.embl.fire.entities.Person;
import com.embl.fire.entities.PersonPatch;
import com.embl.fire.exceptions.EntityNotFoundException;
import com.embl.fire.repositories.PersonRepository;
import com.embl.fire.services.PersonService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    private final PersonRepository personRepository = mock(PersonRepository.class);

    private final PersonService personService = new PersonServiceImpl(personRepository);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Person person = new Person("vivek", "thusu", 26, "yellow");

    @BeforeEach
    @SneakyThrows
    public void beforeEach() {
        List<Person> personList = objectMapper.readValue(
                buildURL("PersonExample.json"), new TypeReference<List<Person>>(){});
        when(personRepository.findAll()).thenReturn(personList);
        when(personRepository.findById(any())).thenReturn(Optional.empty());
        when(personRepository.findById("vivek")).thenReturn(Optional.of(person));
        doNothing().when(personRepository).deleteById(any());
        when(personRepository.save(any())).thenAnswer(invocation
                -> invocation.<Person>getArgument(0));
    }

    private URL buildURL(String resourceName) {
        return this.getClass().getClassLoader().getResource(resourceName);
    }

    @Test
    @DisplayName("findAllPersons")
    void findAllPersons() {
        List<Person> personList = personService.findAllPersons();
        assertEquals(3, personList.size());
        assertEquals("Gates", personList.get(1).getLastName());
    }

    @Test
    @DisplayName("findPersonByFirstName_whenPersonPresent_thenSuccess")
    void findPersonByFirstName_positive() {
        Person person = personService.findPersonByFirstName("vivek");
        assertEquals("yellow", person.getFavouriteColour());
    }

    @Test
    @DisplayName("indPersonByFirstName_whenPersonNotPresent_thenThrowsException")
    void findPersonByFirstName_negative() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.findPersonByFirstName("NameNotPresent"));
    }

    @Test
    @DisplayName("savePerson")
    void savePerson() {
        Person personResponse = personService.savePerson(person);
        assertEquals(person, personResponse);
    }

    @Test
    @DisplayName("deletePersonByFirstName_whenPersonPresent_thenSuccess")
    void deletePersonByFirstName_positive() {
        personService.deletePersonByFirstName("vivek");
        verify(personRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("deletePersonByFirstName_whenPersonNotPresent_thenThrowsException")
    void deletePersonByFirstName_negative() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.deletePersonByFirstName("NameNotPresent"));
    }

    @Test
    @DisplayName("patchPerson_whenPersonPresent_thenPersonUpdated")
    void patchPerson_positive() {
        Person personAfterPatching = personService.patchPerson("vivek", PersonPatch.builder()
                .lastName(JsonNullable.of("lastName"))
                .age(JsonNullable.undefined())
                .favouriteColour(JsonNullable.of("green")).build());
        assertEquals("lastName", personAfterPatching.getLastName());
        assertEquals("green", personAfterPatching.getFavouriteColour());
    }

    @Test
    @DisplayName("patchPerson_whenPersonNotPresent_thenThrowsException")
    void patchPerson_negative() {
        assertThrows(EntityNotFoundException.class,
                () -> personService.patchPerson("NameNotPresent", new PersonPatch()));
    }
}