package com.emsi.server.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.emsi.server.entity.Address;
import com.emsi.server.entity.State;

import jakarta.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AddressRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(AddressRepositoryTest.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StateRepository stateRepository;

    private State marrakechState;
    private State silesianState;

    @BeforeEach
    void setUp() {
        // Load states before tests
        stateRepository.save(State.builder()
        .name("Marrakech-Safi")
        .build());
        stateRepository.save(State.builder()
                .name("Śląskie")
                .build());

        marrakechState = stateRepository.findByName("Marrakech-Safi")
                .orElseThrow(() -> new IllegalStateException("Marrakech-Safi state not found"));
        silesianState = stateRepository.findByName("Śląskie")
                .orElseThrow(() -> new IllegalStateException("Śląskie state not found"));
    }

    @Test
    @Transactional
    void shouldSaveAndVerifyAddress() {
        // Given
        Address address = new Address();
        address.setCity("Marrakech");
        address.setStreet("Amerchich");
        address.setZip_code("40070");
        address.setState(marrakechState);

        // When
        Address savedAddress = addressRepository.save(address);

        // Then
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getCity()).isEqualTo("Marrakech");
        assertThat(savedAddress.getStreet()).isEqualTo("Amerchich");
        assertThat(savedAddress.getZip_code()).isEqualTo("40070");
        assertThat(savedAddress.getState()).isNotNull();
        assertThat(savedAddress.getState().getId()).isEqualTo(marrakechState.getId());
        assertThat(savedAddress.getState().getName()).isEqualTo("Marrakech-Safi");
    }

    @Test
    @Transactional
    void shouldUpdateExistingAddress() {
        // Given
        Address address = new Address();
        address.setCity("Katowice");
        address.setStreet("Krasińskiego 55");
        address.setZip_code("40-100");
        address.setState(silesianState);

        // When
        Address savedAddress = addressRepository.save(address);
        log.info("Original address saved: {}", formatAddressLog(savedAddress));

        // Then
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getCity()).isEqualTo("Katowice");
        assertThat(savedAddress.getStreet()).isEqualTo("Krasińskiego 55");
        assertThat(savedAddress.getZip_code()).isEqualTo("40-100");
        assertThat(savedAddress.getState()).isEqualTo(silesianState);

        // When updating
        savedAddress.setStreet("Warszawska 69");
        Address updatedAddress = addressRepository.save(savedAddress);
        log.info("Updated address saved: {}", formatAddressLog(updatedAddress));

        // Then verify update
        assertThat(updatedAddress).isNotNull();
        assertThat(updatedAddress.getId()).isEqualTo(savedAddress.getId());
        assertThat(updatedAddress.getCity()).isEqualTo("Katowice");
        assertThat(updatedAddress.getStreet()).isEqualTo("Warszawska 69");
        assertThat(updatedAddress.getZip_code()).isEqualTo("40-100");
        assertThat(updatedAddress.getState()).isEqualTo(silesianState);

        // Cleanup
        addressRepository.delete(updatedAddress);
    }

    private String formatAddressLog(Address address) {
        return String.format("ID: %d, City: %s, Street: %s, ZIP: %s, State: %s",
                address.getId(),
                address.getCity(),
                address.getStreet(),
                address.getZip_code(),
                address.getState().getName());
    }
}