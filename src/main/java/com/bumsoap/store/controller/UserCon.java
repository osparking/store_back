package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.exception.ExistingEmailEx;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Admin;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.UserRegisterReq;
import com.bumsoap.store.request.UserUpdateReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.CustomerServ;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.service.worker.WorkerServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(UrlMap.USER)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class UserCon {
    private final ObjMapper objMapper;
    private final AdminServ adminServ;
    private final CustomerServ customerServ;
    private final WorkerServInt workerServ;
    private final UserRepoI userRepo;
    private final UserServInt userServ;

    @GetMapping(UrlMap.GET_USER_DTO_BY_ID)
    public ResponseEntity<ApiResp> getUserDtoById(@PathVariable("id") Long id) {
        try {
            UserDto userDto = userServ.getUserDtoById(id);
            return ResponseEntity.ok(new ApiResp(Feedback.USER_DTO_BY_ID, userDto));
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_ALL)
    public ResponseEntity<ApiResp> getAllUser() {
        try {
            return ResponseEntity.ok().body(
                    new ApiResp("유저 전체 목록", userServ.getUserDtoList()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMap.DELETE_BY_ID)
    public ResponseEntity<ApiResp> delete(@PathVariable("id") Long id) {
        try {
            userServ.deleteById(id);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.USER_DELETE_SUCCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

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

    @PutMapping(UrlMap.UPDATE)
    public ResponseEntity<ApiResp> update(@PathVariable("id") Long id,
                                          @RequestBody UserUpdateReq request) {
        try {
            BsUser user = userServ.getUserById(id);

            user.setFullName(request.getFullName());
            user.setMbPhone(request.getMbPhone());

            switch (request.getUserType().toUpperCase()) {
                case "CUSTOMER":
                    var customer = objMapper.mapToDto(user, Customer.class);
                    user = customerServ.add(customer);
                    break;

                case "WORKER":
                    var worker = objMapper.mapToDto(user, Worker.class);
                    worker.setDept(request.getDept());
                    user = workerServ.add(worker);
                    break;

                default:
                    throw new IllegalArgumentException(Feedback.USER_TYPE_WRONG);
            }
            var userDto = objMapper.mapToDto(user, UserDto.class);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.USER_UPDATE_SUCCESS, userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResp(e.getMessage(), null));
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
