package com.bumsoap.store.repository;

import com.bumsoap.store.util.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoITest {
    @Autowired
    private UserRepoI userRepoI;

    @Test
    void findEmailByUserType() {
        var adminEmail = userRepoI.findEmailByUserType(UserType.ADMIN);
        assertTrue(adminEmail.isPresent());
        assertEquals("jbpark03@gmail.com", adminEmail.get());
    }
}