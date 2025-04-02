package com.bumsoap.store.service.user;

import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public interface UserServInt {
    int toggleEnabledColumn(Long id);

    List<UserDto> getUserDtoList();

    void deleteById(Long id);

    BsUser findById(Long id);

    BsUser getUserById(Long id);

    UserDto getUserDtoById(Long id);

    BsUser getByEmail(@NotBlank String email);

    Map<String, Map<String, Long>> countUsersByMonthAndType();

    long countAll();
}
