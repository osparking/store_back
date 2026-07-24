package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class FeeOther {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal islandAdd; // 벽오지(제주 외 도서 산간)

    private BigDecimal deliFreeMin; // 무배 최소 주문액

    @Column(name = "apply_time", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime applyTime = LocalDateTime.now();

    public FeeOther(BigDecimal islandAdd, BigDecimal deliFreeMin) {
        this.islandAdd = islandAdd;
        this.deliFreeMin = deliFreeMin;
    }
}
