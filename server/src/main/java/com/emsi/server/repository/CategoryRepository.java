package com.emsi.server.repository;

import com.emsi.server.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "categories",path = "categories")
@CrossOrigin(origins = "http://localhost:4200")
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
