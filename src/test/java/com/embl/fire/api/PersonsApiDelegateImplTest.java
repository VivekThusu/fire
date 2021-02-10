package com.embl.fire.api;

import com.embl.fire.entities.Person;
import com.embl.fire.model.PersonDTO;
import com.embl.fire.model.PersonPatchDTO;
import com.embl.fire.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonsApiDelegateImplTest {

    public static final String VIVEK = "vivek";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test @Order(1)
    @SneakyThrows
    @DisplayName("getAllPersonEntities_whenAllParamsAreCorrect_thenSuccessfulResponse")
    void getAllPersonEntities_positiveResponse() {
        mockMvc.perform(get("/embl-fire/persons")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.person").isNotEmpty());
    }

    @Test @Order(2)
    @SneakyThrows
    @DisplayName("getAllPersonEntities_whenHttpMethodIsPut_thenMethodNotAllowed")
    void getAllPersonEntities_negativeResponse() {
        mockMvc.perform(put("/embl-fire/persons")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test @Order(3)
    @SneakyThrows
    @DisplayName("getPersonEntityByFirstName_whenPersonExists_thenSuccessfulResponse")
    void getPersonEntityByFirstName_positive() {
        mockMvc.perform(get("/embl-fire/persons/{firstName}", "JohnTest")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(66));
    }

    @Test @Order(4)
    @SneakyThrows
    @DisplayName("getPersonEntityByFirstName_whenPersonDoesNotExist_thenEntityNotFound")
    void getPersonEntityByFirstName_negative() {
        mockMvc.perform(get("/embl-fire/persons/{firstName}", "randomName")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test @Order(5)
    @SneakyThrows
    @DisplayName("savePersonEntity_whenAllValuesAreCorrect_thenSuccessfullyCreated")
    void savePersonEntity_positive() {
        PersonDTO person = new PersonDTO().firstName(VIVEK)
                .lastName("thusu").age(26).favouriteColour("orange");

        mockMvc.perform(post("/embl-fire/persons")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(person)))
                .andDo(print())
                .andExpect(status().isCreated());

        Person personEntity = personRepository.findById(VIVEK).orElse(new Person());
        assertEquals(person.getFavouriteColour(), personEntity.getFavouriteColour());
    }

    @Test @Order(6)
    @SneakyThrows
    @DisplayName("savePersonEntity_whenAgeIsNegative_thenBadRequest")
    void savePersonEntity_negative() {
        PersonDTO person = new PersonDTO().firstName(VIVEK)
                .lastName("thusu").age(-26).favouriteColour("orange");

        mockMvc.perform(post("/embl-fire/persons")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(person)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test @Order(7)
    @SneakyThrows
    @DisplayName("savePersonEntity_whenLastNameIsNull_thenBadRequest")
    void savePersonEntity_negative2() {
        PersonDTO person = new PersonDTO().firstName(VIVEK)
                .lastName(null).age(26).favouriteColour("orange");

        mockMvc.perform(post("/embl-fire/persons")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(person)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test @Order(8)
    @SneakyThrows
    @DisplayName("updatePersonEntityByFirstName_whenPersonExists_thenSuccessfulResponse")
    void updatePersonEntityByFirstName_positive() {
        PersonPatchDTO personPatch = new PersonPatchDTO()
                .age(105).favouriteColour("turquoise");

        mockMvc.perform(patch("/embl-fire/persons/{firstName}", VIVEK)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(personPatch)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value(VIVEK))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("thusu"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(105))
                .andExpect(MockMvcResultMatchers.jsonPath("$.favourite_colour").value("turquoise"));
    }

    @Test @Order(9)
    @SneakyThrows
    @DisplayName("deletePersonEntityByFirstName_whenPersonExists_thenDeletedSuccessfully")
    void deletePersonEntityByFirstName_positive() {
        mockMvc.perform(delete("/embl-fire/persons/{firstName}", VIVEK)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test @Order(10)
    @SneakyThrows
    @DisplayName("updatePersonEntityByFirstName_whenPersonDoesNotExist_thenNotFound")
    void updatePersonEntityByFirstName_negative() {
        PersonPatchDTO personPatch = new PersonPatchDTO()
                .age(105).favouriteColour("turquoise");

        mockMvc.perform(patch("/embl-fire/persons/{firstName}", VIVEK)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(personPatch)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test @Order(11)
    @SneakyThrows
    @DisplayName("deletePersonEntityByFirstName_whenPersonDoesNotExist_thenNotFound")
    void deletePersonEntityByFirstName_negative() {
        mockMvc.perform(delete("/embl-fire/persons/{firstName}", VIVEK)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}