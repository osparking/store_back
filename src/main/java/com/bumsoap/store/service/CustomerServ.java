package com.bumsoap.store.service;

import com.bumsoap.store.dto.EntityConverter;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.repository.CustomerRepoI;
import com.bumsoap.store.util.BsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServ implements CustomerServInt{
    private final CustomerRepoI customerRepo;
    private final EntityConverter<Customer, UserDto> converter;

    @Override
    public List<UserDto> findAllCustomers() {
        var customers = customerRepo.findAll();
        return customers.stream().map(this::mapCustomerToDtoUser).toList();
    }

    private UserDto mapCustomerToDtoUser(Customer customer) {
        UserDto dtoUser = converter.mapEntityToDto(customer, UserDto.class);

        dtoUser.setAddDate(BsUtils.getLocalDateTimeStr(customer.getAddDate()));
        return dtoUser;
    }

    @Override
    public Customer add(Customer customer) {
        return customerRepo.save(customer);
    }
}
