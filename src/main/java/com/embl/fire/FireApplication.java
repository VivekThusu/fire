package com.embl.fire;

import com.embl.fire.model.PersonDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FireApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireApplication.class, args);
        PersonDTO o = new PersonDTO();
    }

}
