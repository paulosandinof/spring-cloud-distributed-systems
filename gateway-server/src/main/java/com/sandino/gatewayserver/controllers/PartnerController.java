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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/partners")
public class PartnerController {
    private final String partners = "http://PARTNERS-SERVICE/partners";
    private final RestTemplate restTemplate;

    public PartnerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @GetMapping
    public ResponseEntity<Object> getPartners() {
        return restTemplate.exchange(partners,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    @CircuitBreaker(name = "BACKEND", fallbackMethod = "fallback")
    @RateLimiter(name = "BACKEND")
    @Bulkhead(name = "BACKEND")
    @Retry(name = "BACKEND", fallbackMethod = "fallback")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPartner(@PathVariable String id) {
        return restTemplate.exchange(partners + "/" + id,
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
    public ResponseEntity<Object> createPartner(HttpEntity<byte[]> requestEntity) {
        return restTemplate.exchange(partners,
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

    public ResponseEntity<Object> fallback(Exception e) {
        System.out.println(e.getClass());
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body("Internal Server Error");
    }
}
