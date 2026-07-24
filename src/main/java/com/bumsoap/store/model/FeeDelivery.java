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
public class FeeDelivery {
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

    @Column(name = "apply_time", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime applyTime = LocalDateTime.now();

    public FeeDelivery(BoxSize boxSize, BigDecimal areaSame,
                       BigDecimal areaDiff, BigDecimal areaJeju) {
        this.boxSize = boxSize;
        this.areaSame = areaSame;
        this.areaDiff = areaDiff;
        this.areaJeju = areaJeju;
    }
}
