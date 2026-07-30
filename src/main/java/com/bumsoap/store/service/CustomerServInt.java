package com.bumsoap.store.service;

import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.Customer;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CustomerServInt {
    List<UserDto> findAllCustomers();

    @Transactional
    Customer add(Customer customer, boolean wasEnabled);

    SearchResult<UserDto> getCustomerPage(String email, String name,
                                          Integer page, Integer size);
}
