package com.bumsoap.store.service.user;

import com.bumsoap.store.dto.RecipientDto;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserServInt {
    void enable2FA(Long id);

    void disable2FA(Long id);

    boolean verifyCode(Long id, int code);

    GoogleAuthenticatorKey generateSecret(Long id);

    String findDummyEmailWithMaxNum();

    int toggleEnabledColumn(Long id);

    List<UserDto> getUserDtoList();

    String deleteById(Long id);

    BsUser findById(Long id);

    BsUser getUserById(Long id);

    UserDto getUserDtoById(Long id);

    BsUser getByEmail(@NotBlank String email);

    Optional<BsUser> getBsUserByEmail(String email);

    Map<String, Map<String, Long>> countUsersByMonthAndType();

    long countAll();

    RecipientDto getRecipientById(Long id);

    List<Map<String, Object>> getSoapsMonthOfUser(Long id);
}
