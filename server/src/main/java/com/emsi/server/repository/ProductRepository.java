package com.emsi.server.repository;

import com.emsi.server.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "products",path = "products")
@CrossOrigin(origins = "http://localhost:4200")
public interface ProductRepository extends CrudRepository<Product,Long> {
}
