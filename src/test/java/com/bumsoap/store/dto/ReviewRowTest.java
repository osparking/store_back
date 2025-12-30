package com.bumsoap.store.dto;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReviewRowTest {

    @Test
    public void TestDateConverter() {
        LocalDateTime orderTime = LocalDateTime.now();
        String result = ReviewRow.formatKoreanDateTime(orderTime);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

}