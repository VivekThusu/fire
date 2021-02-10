package com.embl.fire.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    //for simplicity sake, firstName is taken as the primary key.
    @Id
    @Column(name = "FIRST_NAME", nullable = false, updatable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "AGE", nullable = false)
    private Integer age;

    @Column(name = "FAVOURITE_COLOUR")
    private String favouriteColour;
}
