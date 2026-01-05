package com.bumsoap.store.service.orderItem;

import com.bumsoap.store.dto.ShapeLabelCount;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderItemRepo;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.SubTotaler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServ implements OrderItemServI {
    private final OrderItemRepo orderItemRepo;
    private final SubTotaler subTotaler;

    @Override
    public OrderItem save(OrderItem item) {
        item.setSubTotal(subTotaler.getSubtotal(item));

        return orderItemRepo.save(item);
    }

    @Override
    public List<OrderItem> saveOrderItems(List<OrderItem> items) {
        return orderItemRepo.saveAll(items);
    }

    @Override
    public List<ShapeLabelCount> findSoapCountByShapeForUser(Long userId) {
        LocalDate today = LocalDate.now();

        // 5개월 전 달의 첫 날 자정 시각 계산
        LocalDateTime fiveMonthsAgo = today
                .minusMonths(5)  // 5개월 전
                .with(TemporalAdjusters.firstDayOfMonth())  // 해당 월의 첫 날
                .atStartOfDay();  // 자정 (00:00:00)

        var results = orderItemRepo.
                findSoapCountByShapeForUser(userId, fiveMonthsAgo);
        List<ShapeLabelCount> countList = null;

        if (results.isEmpty()) {
            countList = Arrays.stream(BsShape.values()).map(shape ->
                    new ShapeLabelCount(shape.label, 0.001f)
            ).collect(Collectors.toList());
        } else {
            countList = results.stream().map(result -> new ShapeLabelCount(
                            result.getShape().label, result.getSoaps()))
                    .collect(Collectors.toList());
        }
        return countList;
    }
}
