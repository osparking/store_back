package com.bumsoap.store.controller;

import com.bumsoap.store.dto.BsOrderDto;
import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.AddressBasis;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.AddOrderReq;
import com.bumsoap.store.request.AddrBasisAddReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.service.order.OrderServI;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequestMapping(UrlMap.ORDER)
@RequiredArgsConstructor
public class OrderCon {
  @Autowired
  private final AddressBasisServI addrBasisServ;
  private final ObjMapper objMapper;
  private final OrderServI orderServ;
  private final UserRepoI userRepo;

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
            new ApiResp(Feedback.NOT_QUALIFIED_FOR + uid, null));
      }
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @PostMapping(UrlMap.ADD)
  public ResponseEntity<ApiResp> addOrder(
      @RequestBody AddOrderReq addOrderReq) {
    try {
      BsOrder order = objMapper.mapToDto(addOrderReq, BsOrder.class);
      var user = userRepo.findById(addOrderReq.getUserId()).orElseThrow(
          () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND));
      order.setUser(user);
      BsOrder orderSaved = orderServ.saveOrder(order);
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
