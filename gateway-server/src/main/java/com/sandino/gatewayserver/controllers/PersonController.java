package com.sandino.gatewayserver.controllers;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/people")
public class PersonController {
    private final String people = "http://PEOPLE-SERVICE/people";
    private final RestTemplate restTemplate;

    public PersonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @GetMapping
    public ResponseEntity<Object> getPeople() {
        Object[] body = restTemplate.getForEntity(people, Object[].class).getBody();
        return ResponseEntity.ok(body);
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPerson(@PathVariable String id) {
        return restTemplate.exchange(people + "/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @PostMapping
    public ResponseEntity<Object> createPerson(HttpEntity<byte[]> requestEntity) {
        return restTemplate.exchange(people,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
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

    public ResponseEntity<Object> fallback(HttpClientErrorException.BadRequest e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    public ResponseEntity<Object> fallback(Exception e) {
        System.out.println(e.getClass());
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body("Internal Server Error");
    }
}
