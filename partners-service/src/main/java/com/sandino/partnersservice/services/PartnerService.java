package com.sandino.partnersservice.services;

import com.sandino.partnersservice.dtos.PartnerRequestDTO;
import com.sandino.partnersservice.entities.Partner;
import com.sandino.partnersservice.exceptions.PartnerNotFoundException;
import com.sandino.partnersservice.repositories.PartnerRepository;
import org.springframework.stereotype.Service;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public Iterable<Partner> list() {
        return this.partnerRepository.findAll();
    }

    public Partner get(String id) throws PartnerNotFoundException {
        return this.partnerRepository.findById(id).orElseThrow(() -> new PartnerNotFoundException("Partner Not Found"));
    }

    public Partner save(PartnerRequestDTO requestDTO) {
        return this.partnerRepository.save(requestDTO.toPartner());
    }
}
