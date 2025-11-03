package com.bumsoap.store.model;

import com.bumsoap.store.util.CardInfo;
import com.bumsoap.store.util.TossPaymentMethod;
import com.bumsoap.store.util.TossPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TossPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentKey;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderId",
            nullable = false)
    private BsOrder order;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    /**
     * 아마 confirm 호출한 시각
     */
    private LocalDateTime approvedAt;

    private String currency; // "KRW"

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TossPaymentMethod method; // "카드"

    /**
     * 금액 멤버들
     */
    private BigDecimal suppliedAmount; // 9,727
    private BigDecimal vat; // 973
    private BigDecimal balanceAmount; // 10,700
    private BigDecimal totalAmount; // 10,700

    private String receiptUrl; // url: https://...tosspayments.com/...

    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "checkout_url"))
    private CardInfo card;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TossPaymentStatus status; // "DONE" 등

    private String version; // "2022-11-16"
}