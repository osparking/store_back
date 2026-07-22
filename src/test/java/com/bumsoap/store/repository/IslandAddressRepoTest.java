package com.bumsoap.store.repository;

import com.bumsoap.store.model.IslandAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class IslandAddressRepoTest {
    @Autowired
    private IslandAddressRepo islandAddressRepo;

    @Test
    void testIslandAddressByZipcodeFinds() {
        Optional<IslandAddress> address = islandAddressRepo.findByZipcode("15654");

        Assertions.assertTrue(address.isPresent());
    }

    @Test
    void testIslandAddressByZipcodeEmpty() {
        Optional<IslandAddress> address = islandAddressRepo.findByZipcode("00000");

        Assertions.assertTrue(address.isEmpty());
    }
}