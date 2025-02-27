package com.bumsoap.store.service;

import com.bumsoap.store.model.Customer;
import com.bumsoap.store.repository.CustomerRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServ {
    private final CustomerRepoI customerRepo;
    public Customer add(Customer customer) {
        return customerRepo.save(customer);
    }
}
