package com.bumsoap.store.service;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServ {
    private final UserRepoI userRepo;
    public BsUser getUserById(Long id) {
        Optional<BsUser> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }
}
