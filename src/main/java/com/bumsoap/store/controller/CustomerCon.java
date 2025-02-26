package com.bumsoap.store.controller;

import com.bumsoap.store.model.Customer;
import com.bumsoap.store.service.CustomerServ;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerCon {
    private final CustomerServ customerServ;

    @PostMapping("/add")
    public void add(@RequestBody Customer customer) {
        customerServ.add(customer);
    }
}
