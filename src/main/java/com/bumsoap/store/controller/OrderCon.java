package com.bumsoap.store.controller;

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
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@RequestMapping(UrlMap.ORDER)
@RequiredArgsConstructor
public class OrderCon {
  @Autowired
  private final AddressBasisServI addrBasisServ;
  private final ObjMapper objMapper;
  private final OrderServI orderServ;
  private final UserRepoI userRepo;

  @PostMapping(UrlMap.ADD)
  public ResponseEntity<ApiResp> addOrder(
      @RequestBody AddOrderReq addOrderReq) {
    try {
      BsOrder order = objMapper.mapToDto(addOrderReq, BsOrder.class);
      var user = userRepo.findById(addOrderReq.getUserId()).orElseThrow(
          () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND));
      order.setUser(user);
      BsOrder orderSaved = orderServ.saveOrder(order);

      return ResponseEntity.ok(new ApiResp(Feedback.SOAP_ORDER_SAVED,
          orderSaved));
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
