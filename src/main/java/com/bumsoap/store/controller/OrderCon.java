package com.bumsoap.store.controller;

import com.bumsoap.store.dto.BsOrderDto;
import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.AddressBasis;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.AddOrderReq;
import com.bumsoap.store.request.AddrBasisAddReq;
import com.bumsoap.store.request.DeliveryFeeReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.service.order.OrderServI;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.OrderStatus;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMap.ORDER)
@RequiredArgsConstructor
public class OrderCon {
    @Autowired
    private final AddressBasisServI addrBasisServ;
    private final ObjMapper objMapper;
    private final OrderServI orderServ;
    private final UserRepoI userRepo;

    @PostMapping(UrlMap.GET_DELIVERY_FEE)
    public ResponseEntity<ApiResp> findDeliveryFee(
            @RequestBody DeliveryFeeReq feeReq) {

        try {
            var deliveryFee = orderServ.findDeliveryFee(
                    feeReq.getGrandTotal(), feeReq.getZipcode());
            return ResponseEntity.ok(
                    new ApiResp(Feedback.DELIVERY_FEE_FOUND, deliveryFee));
        } catch (InventoryException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMap.DELETE_BY_ID)
    public ResponseEntity<ApiResp> delete(@PathVariable("id") Long id) {
        try {
            var order = orderServ.findOrderById(id);
            if (BsUtils.isQualified(order.getUser().getId(), false, null)) {
                orderServ.deleteById(id);
                return ResponseEntity.ok(
                        new ApiResp(Feedback.DELETEED_ORDER_ID + id, null));
            } else {
                return ResponseEntity.status(UNAUTHORIZED).body(
                        new ApiResp(Feedback.NOT_BELONG_TO_YOU + id, null));
            }
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMap.UPDATE2)
    public ResponseEntity<ApiResp> updateOrder(
            @RequestBody AddOrderReq addOrderReq) {
        try {
            BsOrder order = objMapper.mapToDto(addOrderReq, BsOrder.class);
            BsOrder orderSaved = orderServ.updateOrder(order);

            return ResponseEntity.ok(new ApiResp(Feedback.ORDER_UPDATED,
                    objMapper.mapToDto(orderSaved, BsOrderDto.class)));
        } catch (InventoryException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_BY_ID)
    public ResponseEntity<ApiResp> getOrderById(@PathVariable Long id) {
        try {
            // id 로 주문을 읽고 그 주문을 낸 유저의 id 를 uid 로 저장
            BsOrder order = orderServ.findOrderById(id);
            long uid = order.getUser().getId();

            if (BsUtils.isQualified(uid, false, null)) {
                // 주문을 dto 객체로 사상
                var orderDto = objMapper.mapToDto(order, BsOrderDto.class);
                return ResponseEntity.ok(
                        new ApiResp(Feedback.ORDER_FOUND, orderDto));
            } else {
                return ResponseEntity.status(UNAUTHORIZED).body(
                        new ApiResp(Feedback.NOT_BELONG_TO_YOU + id, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.MY_ROWS)
    public ResponseEntity<ApiResp> getMyRows(
            @RequestParam("userId") int userId,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        try {
            var result = orderServ.serviceMyOrders(userId, page, size);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.MY_ORDERS_FOUND, result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResp(Feedback.MY_ORDERS_FAILURE, null));
        }
    }

    @PostMapping(UrlMap.ADD)
    public ResponseEntity<ApiResp> addOrder(
            @RequestBody AddOrderReq addOrderReq) {
        try {
            var recipReq = addOrderReq.getRecipRegiReq();
            var basisReq = recipReq.getAddrBasisAddReq();
            var addrBasis = objMapper.mapToDto(basisReq, AddressBasis.class);
            var recipient = objMapper.mapToDto(recipReq, Recipient.class);

            recipient.setAddressBasis(addrBasis);
            BsOrder order = objMapper.mapToDto(addOrderReq, BsOrder.class);
            order.setOrderStatus(OrderStatus.PAY_WAIT);
            order.setRecipient(recipient);

            var user = userRepo.findById(addOrderReq.getUserId()).orElseThrow(
                    () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND));
            order.setUser(user);
            BsOrder orderSaved = orderServ.saveOrder(order);

            // 기본 수신처 처리 지시 수행
            switch (addOrderReq.getDefaultRecipientAction()) {
                case "store":
                    System.out.println("다음 수신처 ID 를 저장: "
                        + order.getRecipient().getId());
                    break;
                case "remove":
                    System.out.println("기본 수신처 ID 를 제거: "
                        + order.getRecipient().getId());
                    break;
                default:
                    break;
            }

            var orderDto = objMapper.mapToDto(orderSaved, BsOrderDto.class);
            return ResponseEntity.ok(new ApiResp(Feedback.SOAP_ORDER_SAVED,
                    orderDto));
        } catch (InventoryException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @PostMapping(UrlMap.ADD_BASIC_ADDR)
    public ResponseEntity<ApiResp> addBasicAddr(
            @RequestBody AddrBasisAddReq addrBasisAddReq) {
        try {
            var basis = objMapper.mapToDto(addrBasisAddReq, AddressBasis.class);
            var addressBasisSavedOrFromDB = addrBasisServ.addGetAddrBasis(basis);

            return ResponseEntity.ok(new ApiResp(Feedback.BASIC_ADDR_SAVED,
                    addressBasisSavedOrFromDB));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }
}
