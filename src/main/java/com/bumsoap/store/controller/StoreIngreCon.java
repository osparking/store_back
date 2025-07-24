package com.bumsoap.store.controller;

import com.bumsoap.store.dto.EntityConverter;
import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.StoreIngreDto;
import com.bumsoap.store.model.StoreIngre;
import com.bumsoap.store.repository.StoreIngreRepoI;
import com.bumsoap.store.request.IngreStoreReq;
import com.bumsoap.store.request.IngreUpdateReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.store.StoreIngreServI;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMap.STORE_INGRED)
@RequiredArgsConstructor
public class StoreIngreCon {
  private final StoreIngreRepoI storeIngreRepo;
  private final StoreIngreServI storeIngreServI;
  private final EntityConverter<StoreIngre, StoreIngreDto> stInConverter;
  private final ObjMapper objMapper;

  @GetMapping(UrlMap.GET_INGRE_NAMES)
  public ResponseEntity<ApiResp> getIngreNames() {
    return ResponseEntity.ok(new ApiResp(Feedback.FOUND_INGRE_NAMES,
        storeIngreRepo.findDistinctIngreNames()));
  }

  @PutMapping(UrlMap.UPDATE)
  public ResponseEntity<ApiResp> update(@PathVariable("id") Long id,
                                        @RequestBody IngreUpdateReq request) {
    try {
      var storedIngre = storeIngreServI.findById(id);

      if (!BsUtils.isQualified(storedIngre.getWorkerId())) {
        return ResponseEntity.status(UNAUTHORIZED).body(
            new ApiResp(Feedback.NOT_QUALIFIED_ON + id, null));
      }
      storedIngre.setIngreName(request.getIngreName());
      storedIngre.setQuantity(request.getQuantity());
      storedIngre.setPackunit(request.getPackunit());
      storedIngre.setCount(request.getCount());
      storedIngre.setStoreDate(request.getStoreDate());
      storedIngre.setBuyPlace(request.getBuyPlace());
      storedIngre.setExpireDate(request.getExpireDate());
      var storedIng = storeIngreRepo.save(storedIngre);
      var storedIngDto = objMapper.mapToDto(storedIng, StoreIngreDto.class);

      return ResponseEntity.ok(
          new ApiResp(Feedback.INGRE_UPDATE_SUCC, storedIngDto));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(
          new ApiResp(e.getMessage(), null));
    }
  }

  @PostMapping(UrlMap.ADD)
  public ResponseEntity<ApiResp> add(@RequestBody IngreStoreReq request) {
    try {
      var inStRow = objMapper.mapToDto(request, StoreIngre.class);
      var savedRow = storeIngreRepo.save(inStRow);
      return ResponseEntity.ok(
          new ApiResp(Feedback.INGRE_STORE_SUCC, savedRow));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @DeleteMapping(UrlMap.DELETE_BY_ID)
  public ResponseEntity<ApiResp> delete(@PathVariable("id") Long id) {
    try {
      String ingredName = storeIngreServI.deleteById(id);
      return ResponseEntity.ok(
          new ApiResp(Feedback.DELETEED_STORE_INGRE + ingredName, null));
    } catch (Exception e) {
      return ResponseEntity.status(NOT_FOUND)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @GetMapping(UrlMap.GET_ALL)
  public ResponseEntity<ApiResp> getAllStoredIngred() {
    try {
      var storedIngre = storeIngreRepo.findAll();
      List<StoreIngreDto> storeIngreDtos = storedIngre.stream()
          .map(entity -> stInConverter.mapEntityToDto
              (entity, StoreIngreDto.class))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(
          new ApiResp("입고 재료 전체 목록", storeIngreDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
