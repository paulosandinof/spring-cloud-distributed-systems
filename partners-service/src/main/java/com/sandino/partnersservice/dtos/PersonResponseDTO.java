package com.sandino.partnersservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonResponseDTO {
    private String id;
    private String ownerName;
    private String document;
    private String email;
}
