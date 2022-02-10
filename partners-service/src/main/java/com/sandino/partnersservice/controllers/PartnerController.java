package com.sandino.partnersservice.controllers;

import com.sandino.partnersservice.dtos.PartnerRequestDTO;
import com.sandino.partnersservice.dtos.PartnerResponseDTO;
import com.sandino.partnersservice.dtos.PersonResponseDTO;
import com.sandino.partnersservice.entities.Partner;
import com.sandino.partnersservice.exceptions.PersonNotFoundException;
import com.sandino.partnersservice.services.PartnerService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/partners")
public class PartnerController {
    private final String people = "http://PEOPLE-SERVICE/people";
    private final RestTemplate restTemplate;

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService, RestTemplate restTemplate) {
        this.partnerService = partnerService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public Iterable<Partner> index() {
        return partnerService.list();
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @GetMapping("/{id}")
    public ResponseEntity<Object> show(@PathVariable String id) {
        try {
            Partner partner = partnerService.get(id);

            PersonResponseDTO personResponseDTO =
                    restTemplate.getForObject(people + "/" + partner.getUserId(), PersonResponseDTO.class);

            return ResponseEntity.ok(new PartnerResponseDTO(partner, personResponseDTO));
        } catch (PersonNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body("Person Not Found");
        }
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @PostMapping
    public ResponseEntity<Object> store(@RequestBody PartnerRequestDTO requestDTO) {
        try {
            restTemplate.exchange(people + "/" + requestDTO.getUserId(),
                    HttpMethod.GET,
                    null,
                    PersonResponseDTO.class);

            Partner partner = partnerService.save(requestDTO);
            return ResponseEntity.created(URI.create("/partners/" + partner.getId())).body(partner);
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body("Person Not Found");
        }
    }

    public ResponseEntity<Object> fallback(IllegalStateException e) {
        return ResponseEntity.internalServerError().body("Partner Service Not Available");
    }

    public ResponseEntity<Object> fallback(ResourceAccessException e) {
        return ResponseEntity.internalServerError().body("Partner Service Not Available");
    }

    public ResponseEntity<Object> fallback(HttpServerErrorException e) {
        return ResponseEntity.internalServerError().body("People Service Not Available");
    }

    public ResponseEntity<Object> fallback(CallNotPermittedException e) {
        return ResponseEntity.internalServerError().body("Circuit Breaker is OPEN");
    }

    public ResponseEntity<Object> fallback(Exception e) {
        System.out.println(e.getClass());
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body("Internal Server Error");
    }
}
