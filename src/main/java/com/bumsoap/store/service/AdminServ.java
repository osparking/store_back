package com.bumsoap.store.service;

import com.bumsoap.store.model.Admin;
import com.bumsoap.store.repository.AdminRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServ {
    private final AdminRepoI adminRepo;
    public Admin add(Admin admin) {
        return adminRepo.save(admin);
    }
}
