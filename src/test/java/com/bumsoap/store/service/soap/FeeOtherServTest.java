package com.bumsoap.store.service.soap;

import com.bumsoap.store.repository.FeeOtherRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeeOtherServTest {
    @Autowired
    private FeeOtherRepo feeOtherRepo;

    @Test
    void getLatestFeeOther() {
        var feeOther = feeOtherRepo.findLatestFeeOther();
        Assertions.assertNotNull(feeOther);
    }
}