package com.bumsoap.store.controller;

import com.bumsoap.store.request.AddProduceReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.service.produce.ProduceServI;
import com.bumsoap.store.util.AuthUtil;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(UrlMap.WORKER)
@RequiredArgsConstructor
public class ProduceCon {
    private final AuthUtil authUtil;
    private final JwtUtilBean jwtUtilBean;
    private final ProduceServI produceServI;

    @DeleteMapping(UrlMap.DELETE_PRODUCE_BY_ID)
    public ResponseEntity<ApiResp> delete(@PathVariable("id") Long id) {
        try {
            String produceAddedAt = produceServI.deleteById(id);

            return ResponseEntity.ok(new ApiResp(
                    Feedback.DELETEED_PRODUCE + produceAddedAt, null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.PRODUCE_PAGE)
    public ResponseEntity<ApiResp> getProduces(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {

        try {
            var result = produceServI.getProducePage(page, size);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.PRODUCE_PAGE_FOUND, result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResp(Feedback.PRODUCE_PAGE_FAILURE, null));
        }
    }

    @PostMapping(UrlMap.ADD_PRODUCE)
    public ResponseEntity<ApiResp> add(
            @NonNull HttpServletRequest request,
            @RequestBody AddProduceReq addProduceReq) {
        try {
            String jwt = authUtil.getJwtFromRequest(request);
            Long id = jwtUtilBean.getIdFrom(jwt);
            var savedProduce = produceServI.addProduce(id, addProduceReq);

            return ResponseEntity.ok(new ApiResp(
                    Feedback.SOAP_PRODUCE_STORED, savedProduce));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }
}
