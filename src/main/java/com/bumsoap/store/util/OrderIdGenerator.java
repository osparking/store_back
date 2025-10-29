package com.bumsoap.store.util;

import com.bumsoap.store.model.BsOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;

@Repository
public class OrderIdGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    public String generateOrderId(BsOrder order) {
        entityManager.persist(order);
        entityManager.flush();
        return String.format("%08d", order.getId()) + getOrderIdSuffix();
    }

    private String getOrderIdSuffix() {
        int length = 6;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String LOWER = "abcdefghijklmnopqrstuvwxyz";
            String CHARS = UPPER + LOWER + "0123456789_-";
            sb.append(CHARS.charAt(secureRandom.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}