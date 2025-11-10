package com.bumsoap.store.repository;

import com.bumsoap.store.model.BsOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepoClass {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteOrdersByUserIdWithoutPayments(Long userId) {
        // First, find the orders that match your criteria
        String jpql = """
                SELECT bo FROM BsOrder bo 
                LEFT JOIN TossPayment tp ON bo.orderId = tp.orderId 
                WHERE bo.userId = :userId AND tp.orderId IS NULL
                """;

        List<BsOrder> ordersToDelete = entityManager
                .createQuery(jpql, BsOrder.class)
                .setParameter("userId", userId)
                .getResultList();

        // Delete them - JPA will handle cascade deletion of OrderItems
        for (BsOrder order : ordersToDelete) {
            entityManager.remove(order);
        }
    }
}