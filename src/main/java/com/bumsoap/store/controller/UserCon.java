package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.exception.ExistingEmailEx;
import com.bumsoap.store.model.Admin;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.UserRegisterReq;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.CustomerServ;
import com.bumsoap.store.service.UserServ;
import com.bumsoap.store.service.WorkerServ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserCon {
    private final ObjMapper objMapper;
    private final AdminServ adminServ;
    private final CustomerServ customerServ;
    private final WorkerServ workerServ;
    private final UserRepoI userRepo;
    private final UserServ userServ;

    @GetMapping("/{id}")
    public ResponseEntity<BsUser> getUser(@PathVariable("id") Long id) {
       BsUser user = userServ.getUserById(id);
       if (user == null) {
           return ResponseEntity.notFound().build();
       } else {
           return ResponseEntity.ok(user);
       }
    }

    @PostMapping("/add")
    public BsUser add(@RequestBody UserRegisterReq request) {
        String email = request.getEmail();
        BsUser user = null;

        if (userRepo.existsByEmail(email)) {
            throw new ExistingEmailEx("오류 - 선점된 이메일: " + email);
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
                throw new IllegalArgumentException("존재하지 않는 유저 유형");
        }
        return user;
    }
}
