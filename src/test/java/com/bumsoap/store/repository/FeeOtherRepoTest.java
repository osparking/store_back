package com.bumsoap.store.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeeOtherRepoTest {
    @Autowired
    private FeeOtherRepo feeOtherRepo;

    @Test
    void testIfFeeReadsWell() {
        var feeOther = feeOtherRepo.findLatestFeeOther();

        Assertions.assertNotNull(feeOther);
    }

}