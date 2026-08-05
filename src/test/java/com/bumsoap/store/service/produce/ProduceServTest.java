package com.bumsoap.store.service.produce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProduceServTest {
    @Autowired
    private ProduceServI produceServI;

    @Test
    void getSoapProduceChart() {
        var chartData = produceServI.getSoapProduceChart();
        Assertions.assertEquals(6, chartData.size());
    }
}