package com.bumsoap.store.repository;

import com.bumsoap.store.row.CartItemRow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CartItemRepoTest {
    @Autowired
    private CartItemRepo cartItemRepo;

    @Test
    void getUserCartById() {
        List<CartItemRow> items = cartItemRepo.findByUserId(4L);

        Assertions.assertEquals(2, items.size());
    }

}