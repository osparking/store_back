package com.bumsoap.store.service.password;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.PasswordChangeReq;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class PasswordChangeServ implements PasswordChangeServInf{
    private final UserRepoI userRepo;

    @Override
    public void changePwd(Long userId, PasswordChangeReq request) {
        BsUser user = userRepo.findById(userId).orElseThrow(
                () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + userId));

        if (request.getCurPwd().equals("") || request.getNewPwd().equals("")) {
            throw new IllegalArgumentException(Feedback.SOME_FIELD_MISSING);
        }

        boolean matches = request.getCurPwd().equals(user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException(Feedback.CUR_PASSWORD_WRONG);
        }

        if (!Objects.equals(request.getNewPwd(), request.getCnfPwd())) {
            throw new IllegalArgumentException(Feedback.CNF_PASSWORD_WRONG);
        }

        user.setPassword(request.getNewPwd());
        userRepo.save(user);
    }
}
