package com.bumsoap.store.service.user;

import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;

import java.util.List;

public interface UserServInt {
    List<UserDto> getUserDtoList();

    void deleteById(Long id);

    BsUser findById(Long id);

    BsUser getUserById(Long id);
}
