package com.sandino.peopleservice.services;

import com.sandino.peopleservice.dtos.PersonRequestDTO;
import com.sandino.peopleservice.entities.Person;
import com.sandino.peopleservice.exceptions.PersonNotFoundException;
import com.sandino.peopleservice.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Iterable<Person> list() {
        return this.personRepository.findAll();
    }

    public Person get(String id) throws PersonNotFoundException {
        return this.personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person Not Found"));
    }

    public Person save(PersonRequestDTO requestDTO) {
        // Send email
        return this.personRepository.save(requestDTO.toPerson());
    }
}
