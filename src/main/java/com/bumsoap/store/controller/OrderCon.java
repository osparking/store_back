package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.model.AddressBasis;
import com.bumsoap.store.request.AddrBasisAddReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@RequestMapping(UrlMap.ORDER)
@RequiredArgsConstructor
public class OrderCon {
  @Autowired
  private final AddressBasisServI addrBasisServ;
  private final ObjMapper objMapper;

  @PostMapping(UrlMap.ADD_BASIC_ADDR)
  public ResponseEntity<ApiResp> addBasicAddr(
      @RequestBody AddrBasisAddReq addressBasis) {
    try {
      var addr = addrBasisServ.findByRoadAddress(addressBasis.getRoadAddress());

      if (addr == null) { // 주소 인자를 테이블에 추가.
        var basis = objMapper.mapToDto(addressBasis, AddressBasis.class);
        return ResponseEntity.ok(new ApiResp(Feedback.BASIC_ADDR_STORED,
            addrBasisServ.saveUpdate(basis)));
      } else { // 테이블에서 읽은 주소를 반환.
        return ResponseEntity.ok(new ApiResp(Feedback.BASIC_ADDR_FOUND,
            addr));
      }
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
