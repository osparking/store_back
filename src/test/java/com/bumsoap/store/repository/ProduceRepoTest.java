package com.bumsoap.store.repository;

import com.bumsoap.store.row.SoapStock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProduceRepoTest {
    @Autowired
    private ProduceRepo produceRepo;


    @Test
    void getSoapStock() {
        List<SoapStock> soapStocks = produceRepo.getSoapStock();
        assertEquals(3, soapStocks.size());
    }
}