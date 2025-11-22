package com.bumsoap.store.service;

import com.bumsoap.store.exception.OrderIdNotFoundEx;
import com.bumsoap.store.exception.PaymentArgException;
import com.bumsoap.store.model.TossPayment;
import com.bumsoap.store.service.order.OrderServ;
import com.bumsoap.store.util.CardInfo;
import com.bumsoap.store.util.OrderStatus;
import com.bumsoap.store.util.TossPaymentMethod;
import com.bumsoap.store.util.TossPaymentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Transactional
public class PaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderServ orderService;

    public TossPayment createPayment(JSONObject paymentJSON)
            throws OrderIdNotFoundEx, PaymentArgException {

        var payment = new TossPayment();
        payment.setPaymentKey((String) paymentJSON.get("paymentKey"));

        /**
         * 일단 orderId 를 사용하여 bsOrder 객체를 읽고, 이를 배정한다.
         */
        String orderId = (String) paymentJSON.get("orderId");
        var soapOrder = orderService.getOrderByOrderId(orderId);
        payment.setOrder(soapOrder);

        payment.setSuppliedAmount(BigDecimal.valueOf(
                (Long) paymentJSON.get("suppliedAmount")));

        var requestedAtODT = OffsetDateTime.parse(
                (String) paymentJSON.get("requestedAt"));
        payment.setRequestedAt(requestedAtODT.toLocalDateTime());

        payment.setCurrency((String) paymentJSON.get("currency"));

        payment.setPaymentKey((String) paymentJSON.get("paymentKey"));

        var method = TossPaymentMethod
                .valueOfLabel((String) paymentJSON.get("method"));
        payment.setMethod(method);

        payment.setVat(BigDecimal.valueOf((Long) paymentJSON.get("vat")));

        var approvedAtODT = OffsetDateTime.parse(
                (String) paymentJSON.get("approvedAt"));
        payment.setApprovedAt(approvedAtODT.toLocalDateTime());

        payment.setBalanceAmount(BigDecimal.valueOf(
                (Long) paymentJSON.get("balanceAmount")));
        payment.setVersion((String) paymentJSON.get("version"));

        payment.setTotalAmount(BigDecimal.valueOf(
                (Long) paymentJSON.get("totalAmount")));

        JSONObject recepitJSON = (JSONObject) paymentJSON.get("receipt");
        payment.setReceiptUrl((String) recepitJSON.get("url"));

        if (method==TossPaymentMethod.CARD) {
            payment.setCard(getCardInfo((JSONObject) paymentJSON.get("card")));
        }

        var status = TossPaymentStatus
                .valueOf((String) paymentJSON.get("status"));
        payment.setStatus(status);

        try {
            entityManager.persist(payment);
            if (status == TossPaymentStatus.DONE) {
                soapOrder.setOrderStatus(OrderStatus.PAID);
            }
        } catch (IllegalArgumentException e) {
            throw new PaymentArgException(e.getMessage() + " - 결제정보");
        }
        return payment;
    }

    private CardInfo getCardInfo(JSONObject cardJSON) {
        CardInfo card = new CardInfo();
        card.setCardOwnerType((String) cardJSON.get("ownerType"));
        card.setCardNumber((String) cardJSON.get("number"));
        card.setCardAmount(BigDecimal.valueOf((Long) cardJSON.get("amount")));
        card.setCardType((String) cardJSON.get("cardType"));
        card.setCardIssuerCode((String) cardJSON.get("issuerCode"));

        return card;
    }
}
