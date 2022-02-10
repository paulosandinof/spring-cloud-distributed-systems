package com.sandino.peopleservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Person {

    @Id
    private String id;
    private final String ownerName;
    private final String document;
    private final String email;
}