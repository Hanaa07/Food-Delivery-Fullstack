package com.emsi.server.repository;

import com.emsi.server.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "order_items",path = "order_items")
@CrossOrigin(origins = "http://localhost:4200")

public interface OrderItemRepository extends CrudRepository<OrderItem,Long> {
}
