package com.sandino.peopleservice.dtos;

import com.sandino.peopleservice.entities.Person;
import lombok.Data;

@Data
public class PersonRequestDTO {
    private final String ownerName;
    private final String document;
    private final String email;

    public Person toPerson() {
        return new Person(ownerName, document, email);
    }
}
