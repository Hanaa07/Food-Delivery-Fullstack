package com.emsi.server.repository;

import com.emsi.server.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "addresses",path = "addresses")
@CrossOrigin(origins = "http://localhost:4200")
public interface AddressRepository extends CrudRepository<Address, Long> {
    Iterable<Address> findAll();
    Optional<Address> findById(Long id);
}