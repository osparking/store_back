package com.bumsoap.store.service;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServ implements UserServInt {
    private final UserRepoI userRepo;

    @Override
    public void deleteById(Long id) {
        Optional<BsUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.deleteById(id);
        } else {
            throw new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id);
        }
    }

    @Override
    public BsUser findById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id));
    }

    @Override
    public BsUser getUserById(Long id) {
        Optional<BsUser> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }
}
