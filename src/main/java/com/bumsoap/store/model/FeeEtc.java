package com.bumsoap.store.model;

import com.bumsoap.store.util.BoxSize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class FeeEtc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BoxSize boxSize; // 상자 내 비누 수량(예, 3개, 12개)

    @Column(nullable = false)
    private BigDecimal areaSame; // 동일 권역

    @Column(nullable = false)
    private BigDecimal areaDiff; // 다른 권역

    @Column(nullable = false)
    private BigDecimal areaJeju; // 제주 권역

    private BigDecimal island; // 벽오지(제주 외 도서 산간)

    private BigDecimal deliFreeMin; // 무배 최소 주문액

    @Column(name = "apply_time", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime applyTime = LocalDateTime.now();

    public FeeEtc(BoxSize boxSize, BigDecimal areaSame,
                  BigDecimal areaDiff, BigDecimal areaJeju,
                  BigDecimal island, BigDecimal deliFreeMin) {
        this.boxSize = boxSize;
        this.areaSame = areaSame;
        this.areaDiff = areaDiff;
        this.areaJeju = areaJeju;
        this.island = island;
        this.deliFreeMin = deliFreeMin;
    }
}
