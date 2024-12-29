package com.emsi.server.repository;

import com.emsi.server.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepo;

    @Test
    @Transactional
    public void insertAndCheckUser()
    {
        User user = new User();
        user.setName("Test");
        user.setLast_name("TestoweNazwisko");
        user.setEmail("testowetututu@gmail.com");
        user.setRole("customer");
        user.setPhone_number(456789123);
        log.info("USER: "+user.getId()+" "+user.getName()+" "+user.getLast_name()+" "+user.getEmail()+" "+user.getRole());
        User savedUser = userRepo.save(user);

        assertThat(savedUser.getId()).isNotEqualTo(null);
        assertThat(savedUser.getName()).isEqualTo("Test");
        assertThat(savedUser.getLast_name()).isEqualTo("TestoweNazwisko");
        assertThat(savedUser.getEmail()).isEqualTo("testowetututu@gmail.com");
        assertThat(savedUser.getRole()).isEqualTo("customer");
        assertThat(savedUser.getPhone_number()).isEqualTo(user.getPhone_number());
        log.info("USER: "+savedUser.getId()+" "+savedUser.getName()+" "+savedUser.getLast_name()+" "+savedUser.getEmail()+" "+savedUser.getRole());

        userRepo.delete(savedUser);
    }

    @Test
    @Transactional
    public void insertUpdateAndCheckUser()
    {
        User user = new User();
        user.setName("Test");
        user.setLast_name("TestoweNazwisko");
        user.setEmail("testowetututu@gmail.com");
        user.setRole("customer");
        user.setPhone_number(456789123);
        log.info("USER: "+user.getId()+" "+user.getName()+" "+user.getLast_name()+" "+user.getEmail()+" "+user.getRole());
        User savedUser = userRepo.save(user);

        assertThat(savedUser.getId()).isNotEqualTo(null);
        assertThat(savedUser.getName()).isEqualTo("Test");
        assertThat(savedUser.getLast_name()).isEqualTo("TestoweNazwisko");
        assertThat(savedUser.getEmail()).isEqualTo("testowetututu@gmail.com");
        assertThat(savedUser.getRole()).isEqualTo("customer");
        assertThat(savedUser.getPhone_number()).isEqualTo(user.getPhone_number());
        log.info("savedUser: "+savedUser.getId()+" "+savedUser.getName()+" "+savedUser.getLast_name()+" "+savedUser.getEmail()+" "+savedUser.getRole());

        savedUser.setName("TestZmiana");
        savedUser.setLast_name("TestoweNazwiskoZmiana");
        User changedUser = userRepo.save(savedUser);

        assertThat(changedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(changedUser.getName()).isEqualTo("TestZmiana");
        assertThat(changedUser.getLast_name()).isEqualTo("TestoweNazwiskoZmiana");
        assertThat(changedUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(changedUser.getRole()).isEqualTo(savedUser.getRole());
        assertThat(changedUser.getPhone_number()).isEqualTo(savedUser.getPhone_number());
        log.info("changedUser: "+changedUser.getId()+" "+changedUser.getName()+" "+changedUser.getLast_name()+" "+changedUser.getEmail()+" "+changedUser.getRole());

        userRepo.delete(savedUser);

    }
}
