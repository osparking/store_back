package com.bumsoap.store.repository;

import com.bumsoap.store.util.BoxSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeeEtcRepoTest {
    @Autowired
    private FeeEtcRepo feeEtcRepo;

    @Test
    void testIfFeeReadsWell() {
        var feeEtc = feeEtcRepo.findLatestFee(BoxSize.BOX_03);

        Assertions.assertNotNull(feeEtc);
    }

}