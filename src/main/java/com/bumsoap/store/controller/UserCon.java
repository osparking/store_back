package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.exception.ExistingEmailEx;
import com.bumsoap.store.model.Admin;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.UserRegisterReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.CustomerServ;
import com.bumsoap.store.service.UserServ;
import com.bumsoap.store.service.WorkerServ;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.USER)
@RequiredArgsConstructor
public class UserCon {
    private final ObjMapper objMapper;
    private final AdminServ adminServ;
    private final CustomerServ customerServ;
    private final WorkerServ workerServ;
    private final UserRepoI userRepo;
    private final UserServInt userServ;

    @GetMapping(UrlMap.GET_BY_ID)
    public ResponseEntity<ApiResp> getUser(@PathVariable("id") Long id) {
       try {
           BsUser user = userServ.getUserById(id);
           if (user == null) {
               String msg = "존재하지 않는 아이디: " + id;
               return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND)
                       .body(new ApiResp(msg, null));
           } else {
               return ResponseEntity.ok(new ApiResp("유저 발견됨", user));
           }
       } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                   .body(new ApiResp(e.getMessage(), null));
       }
    }

    @PostMapping(UrlMap.ADD)
    public ResponseEntity<ApiResp> add(@RequestBody UserRegisterReq request) {
        String email = request.getEmail();
        BsUser user = null;

        try {
            if (userRepo.existsByEmail(email)) {
                throw new ExistingEmailEx(Feedback.USER_TAKEN_EMAIL + email);
            }
            switch (request.getUserType().toUpperCase()) {
                case "ADMIN":
                    var admin = objMapper.mapToDto(request, Admin.class);
                    user = adminServ.add(admin);
                    break;
                case "CUSTOMER":
                    var customer = objMapper.mapToDto(request, Customer.class);
                    user = customerServ.add(customer);
                    break;
                case "WORKER":
                    var worker = objMapper.mapToDto(request, Worker.class);
                    user = workerServ.add(worker);
                    break;
                default:
                    throw new IllegalArgumentException(Feedback.USER_TYPE_WRONG);
            }
            var userDto = objMapper.mapToDto(user, UserDto.class);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.USER_ADD_SUCCESS, userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResp(e.getMessage(), null));
        }

    }
}
