package com.bumsoap.store.service;

import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.Customer;

import java.util.List;

public interface CustomerServInt {
    List<UserDto> findAllCustomers();

    Customer add(Customer customer);
}
