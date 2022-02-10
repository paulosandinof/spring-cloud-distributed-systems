package com.sandino.peopleservice.controllers;

import com.sandino.peopleservice.dtos.PersonRequestDTO;
import com.sandino.peopleservice.entities.Person;
import com.sandino.peopleservice.exceptions.PersonNotFoundException;
import com.sandino.peopleservice.services.PersonService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public Iterable<Person> index() {
        return personService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        try {
            return ResponseEntity.ok(personService.get(id));
        } catch (PersonNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body("Person not found");
        }
    }

    @PostMapping
    public Person store(@RequestBody PersonRequestDTO requestDTO) {
        return personService.save(requestDTO);
    }

}
