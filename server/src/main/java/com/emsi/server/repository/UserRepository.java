package com.emsi.server.repository;

import com.emsi.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "users",path = "users")
@CrossOrigin(origins = "http://localhost:4200")
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String theEmail);
}