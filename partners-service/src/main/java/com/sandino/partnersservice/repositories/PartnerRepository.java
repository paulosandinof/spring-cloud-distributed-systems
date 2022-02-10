package com.sandino.partnersservice.repositories;

import com.sandino.partnersservice.entities.Partner;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends PagingAndSortingRepository<Partner, String> {

}
