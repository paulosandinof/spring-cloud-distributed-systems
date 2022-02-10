package com.sandino.peopleservice.repositories;

import com.sandino.peopleservice.entities.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, String> {
}
