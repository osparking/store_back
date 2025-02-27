package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.model.Admin;
import com.bumsoap.store.request.UserRegisterReq;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.CustomerServ;
import com.bumsoap.store.service.WorkerServ;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserCon {
    private final ObjMapper objMapper;
    private final AdminServ adminServ;

    @PostMapping("/add")
    public void add(@RequestBody UserRegisterReq request) {
        switch (request.getUserType().toUpperCase()) {
            case "ADMIN":
                var admin = objMapper.mapToDto(request, Admin.class);
                adminServ.add(admin);
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 유저 유형");
        }
    }
}
