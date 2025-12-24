package com.bumsoap.store.controller;

import com.bumsoap.store.exception.WrongSignatureException;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.order.OrderServI;
import com.bumsoap.store.track.TrackingRequest;
import com.bumsoap.store.util.AuthUtil;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.OrderStatus;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.WEBHOOK)
@RequiredArgsConstructor
public class WebhookCon {

    private static final Logger logger =
            LoggerFactory.getLogger(WebhookCon.class);
    private final OrderServI orderServ;

    @Value("${trackingmore.webhook-secret}")
    private String trackingmoreWebhookSecret;

    @PostMapping(UrlMap.TRACK_MORE)
    public ResponseEntity<ApiResp> trackingMoreWebhook(
            HttpServletRequest request,
            @RequestBody TrackingRequest deliveryTracking) {
        boolean result = true;

        try {
            verifySignature(request);
            String status = deliveryTracking.getData().getDelivery_status();

            logger.debug("배송상태: {}, 주문번호: {}, 운송장번호: {}.", status,
                    deliveryTracking.getData().getOrder_number(),
                    deliveryTracking.getData().getTracking_number());

            if ("delivered".equals(status)) {
                // 주문 상태 '배송완료'로 갱신.
                String oIdStr = deliveryTracking.getData().getOrder_number();

                result = orderServ.updateOrderStatus(
                        Long.parseLong(oIdStr), OrderStatus.DELIVERED);
            }

            return ResponseEntity.ok(new ApiResp(Feedback.DELIVERY_STATUS,
                    result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), false));
        }
    }

    private void verifySignature(HttpServletRequest request) throws Exception {
        String signature = request.getHeader("Signature");
        String timestamp = request.getHeader("Timestamp");
        String signatureRight = AuthUtil.generateSignature(timestamp,
                trackingmoreWebhookSecret);
        if (!signatureRight.equals(signature)) {
            throw new WrongSignatureException("트래킹모어 서명이 잘못되었음");
        }
    }
}
