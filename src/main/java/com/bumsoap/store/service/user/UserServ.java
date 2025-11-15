package com.bumsoap.store.service.user;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.RecipientDto;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.service.TotpService;
import com.bumsoap.store.util.Feedback;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServ implements UserServInt {
    private final UserRepoI userRepo;
    private final ObjMapper mapper;
    private final TotpService totpService;

    @Override
    public RecipientDto getRecipientById(Long id) {
        return userRepo.getRecipientById(id).orElse(null);
    }

    @Override
    public void enable2FA(Long id) {
        BsUser user = userRepo.findById(id).orElseThrow(
            () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id));
        user.setTwoFaEnabled(true);
        userRepo.save(user);
    }

    @Override
    public void disable2FA(Long id) {
        BsUser user = userRepo.findById(id).orElseThrow(
            () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id));
        user.setTwoFaEnabled(false);
        userRepo.save(user);
    }

    @Override
    public boolean verifyCode(Long id, int code) {
        BsUser user = userRepo.findById(id).orElseThrow(() ->
            new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id));
        return totpService.verifyCode(user.getTwoFaSecret(), code);
    }

    /**
     * 2FA 체계에서 유저를 위해 사용할 비밀을 생성하고, 유저 테이블에
     * 저장하는 작업도 병행한다.
     * @param id
     * @return
     */
    @Override
    public GoogleAuthenticatorKey generateSecret(Long id) {
        BsUser user = userRepo.findById(id).orElseThrow(() ->
            new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id));
        var secret = totpService.generateSecret();
        user.setTwoFaSecret(secret.getKey());
        userRepo.save(user);

        return secret;
    }

    @Override
    public String findDummyEmailWithMaxNum() {
        return userRepo.findDummyEmailWithMaxNum().orElse(null);
    }

    @Override
    public int toggleEnabledColumn(Long id) {
        return userRepo.toggleEnabledColumn(id);
    }

    @Override
    public List<UserDto> getUserDtoList() {
        return userRepo.findAllUserDto();
    }

    @Override
    public String deleteById(Long id) {
        var user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return user.get().getFullName();
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

    @Override
    public UserDto getUserDtoById(Long id) {
        Optional<UserDto> userOptional = userRepo.findUserDtoById(id);

        if (userOptional.isPresent()) {
            UserDto userDto = userOptional.get();
            return userDto;
        } else {
            throw new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id);
        }
    }

    @Override
    public BsUser getByEmail(String email) {
        Optional<BsUser> user = userRepo.findByEmail(email);
        return user.orElseThrow(() -> new DataNotFoundException(
                Feedback.NOT_FOUND_EMAIL + email));
    }

    @Override
    public Optional<BsUser> getBsUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Map<String, Map<String, Long>> countUsersByMonthAndType() {
        List<BsUser> users = userRepo.findAll();
        var mapMonthType = users.stream().collect(Collectors.groupingBy(
                BsUser::addedMonth,
                Collectors.groupingBy(user -> user.getUserType().label,
                        Collectors.counting())));
        var sorted = new TreeMap<String, Map<String, Long>>();
        sorted.putAll(mapMonthType);
        return sorted;
    }

    @Override
    public long countAll() {
        return userRepo.count();
    }
}
