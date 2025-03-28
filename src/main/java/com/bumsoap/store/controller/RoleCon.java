package com.bumsoap.store.controller;

import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping(UrlMap.ROLE)
public class RoleCon {
    private final RoleServInt roleServ;
}
