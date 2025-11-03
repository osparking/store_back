package com.bumsoap.store.util;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CardInfo {
    private String cardOwnerType; // "개인
    private String cardNumber; // "46731099****758*"
    private BigDecimal cardAmount; // 10,700
    private String cardType; // "체크"
    private String cardIssuerCode; // "11"
}