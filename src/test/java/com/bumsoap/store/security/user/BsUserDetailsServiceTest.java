package com.bumsoap.store.security.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BsUserDetailsServiceTest {

    @Autowired
    private BsUserDetailsService bsUserDetailsService;

    @Test
    void loadUserByUsername() {
        String email = "jbpark03@gmail.com";
        var userDetails = bsUserDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(email, userDetails.getUsername());
    }
}