package com.sandino.partnersservice.dtos;

import lombok.Data;

@Data
public class PersonRequestDTO {
    private final String ownerName;
    private final String document;
    private final String email;
}
