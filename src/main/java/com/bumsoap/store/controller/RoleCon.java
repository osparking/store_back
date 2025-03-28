package com.bumsoap.store.controller;

import com.bumsoap.store.model.Role;
import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(UrlMap.ROLE)
public class RoleCon {
    private final RoleServInt roleServ;

    @GetMapping(UrlMap.GET_ALL)
    public List<Role> getAllRole() {
        return roleServ.getRoles();
    }

    @GetMapping(UrlMap.GET_BY_ID)
    public Role getRoleById(Long id) {
        return roleServ.findById(id);
    }
}
