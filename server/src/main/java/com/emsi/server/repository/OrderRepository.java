package com.emsi.server.repository;

import com.emsi.server.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "orders",path = "orders")
@CrossOrigin(origins = "http://localhost:4200")
public interface OrderRepository extends CrudRepository<Order,Long> {

}
