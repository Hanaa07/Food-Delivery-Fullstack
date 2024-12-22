package com.emsi.server.repository;

import com.emsi.server.entity.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "states",path = "states")
@CrossOrigin(origins = "http://localhost:4200")
public interface StateRepository extends CrudRepository<State,Long>{
}
