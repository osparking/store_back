package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.model.AddressBasis;
import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.request.RecipRegiReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.service.recipient.RecipientServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(UrlMap.RECIPIENT)
@RequiredArgsConstructor
public class RecipientCon {
  private final ObjMapper objMapper;
  private final RecipientServI recipientServ;
  private final AddressBasisServI addressBasisServ;

  @GetMapping(UrlMap.GET_BY_ID)
  public ResponseEntity<ApiResp> getById(@PathVariable("id") long id) {
    try {
      Recipient recipient = recipientServ.findById(id);
      return ResponseEntity.ok(
          new ApiResp(Feedback.RECIPIENT_FOUND, recipient));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(
          new ApiResp(e.getMessage(), null));
    }
  }

  @PostMapping(UrlMap.SAVE_RECIPIENT)
  public ResponseEntity<ApiResp> save(@RequestBody RecipRegiReq request) {
    try {
      var addressBasis = objMapper
          .mapToDto(request.getAddrBasisAddReq(), AddressBasis.class);
      var addrBasisSaved = addressBasisServ.addAddressBasis(addressBasis);

      Recipient recipient = objMapper.mapToDto(request, Recipient.class);
      recipient.setAddressBasis(addrBasisSaved);

      var savedRecipient = recipientServ.save(recipient);
      return ResponseEntity.ok(
          new ApiResp(Feedback.RECIPIENT_SAVED, savedRecipient));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(
          new ApiResp(e.getMessage(), null));
    }
  }
}
