package com.bumsoap.store.service.password;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.PasswordChangeReq;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordChangeServ {
    private final UserRepoI userRepo;

    public void changePwd(Long userId, PasswordChangeReq request) {
        BsUser user = userRepo.findById(userId).orElseThrow(
                () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + userId));

        user.setPassword(request.getNewPwd());
        userRepo.save(user);
    }
}
