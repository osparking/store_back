package com.bumsoap.store.service.token;

import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.VerifinTokenRepoI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class VerifinTokenServTest {
    String token = "08f15030-2ce1-47ab-98b0-c2de6dffac8b";

    @Autowired
    private VerifinTokenRepoI verifinTokenRepo;
    @Autowired
    private VerifinTokenServInt verifinTokenServ;

    @Test
    void findUserByGoodToken() {
        Optional<BsUser> user =
                verifinTokenRepo.findUserByVerificationToken(token);

        Assertions.assertTrue(user.isPresent());
    }

    @Test
    void findUserByWrongToken() {
        Optional<BsUser> user =
                verifinTokenRepo.findUserByVerificationToken("wrongtoken");

        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void findUserByToken() {
        assertThrows(DataNotFoundException.class, () -> {
            verifinTokenServ.findUserByToken(token + "?");
        });
    }

}