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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.WORKER)
@RequiredArgsConstructor
public class ProduceCon {
    private final AuthUtil authUtil;
    private final JwtUtilBean jwtUtilBean;
    private final ProduceServI produceServI;

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
