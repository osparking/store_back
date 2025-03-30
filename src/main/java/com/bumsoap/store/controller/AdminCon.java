package com.bumsoap.store.controller;

import com.bumsoap.store.model.Admin;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMap.ADMIN)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AdminCon {
    private final AdminServ adminServ;
    private final UserServInt userServ;

    @PostMapping("/add")
    public void add(@RequestBody Admin admin) {
        adminServ.add(admin);
    }

    @GetMapping(UrlMap.USER_COUNT)
    public long countAllUsers() {
        return userServ.countAll();
    }
}
