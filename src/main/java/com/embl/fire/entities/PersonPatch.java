package com.embl.fire.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonPatch {

    private JsonNullable<String> lastName;
    private JsonNullable<Integer> age;
    private JsonNullable<String> favouriteColour;

    public Person applyPatch(Person person) {
        return Person.builder()
                .firstName(person.getFirstName())
                .lastName(lastName.orElse(person.getLastName()))
                .age(age.orElse(person.getAge()))
                .favouriteColour(favouriteColour.orElse(person.getFavouriteColour()))
                .build();
    }
}
