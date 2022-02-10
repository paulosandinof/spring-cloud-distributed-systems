package com.sandino.partnersservice.services;

import com.sandino.partnersservice.dtos.PartnerRequestDTO;
import com.sandino.partnersservice.dtos.PersonRequestDTO;
import com.sandino.partnersservice.entities.Partner;
import com.sandino.partnersservice.exceptions.PersonNotFoundException;
import com.sandino.partnersservice.repositories.PartnerRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public Iterable<Partner> list() {
        return this.partnerRepository.findAll();
    }

    public Partner get(String id) throws PersonNotFoundException {
        return this.partnerRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("Person Not Found"));
    }

    public Partner save(PartnerRequestDTO requestDTO) {
        return this.partnerRepository.save(requestDTO.toPartner());
    }
}
