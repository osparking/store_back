package com.bumsoap.store.service;

import com.bumsoap.store.dto.EntityConverter;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.Customer;
import com.bumsoap.store.repository.CustomerRepoI;
import com.bumsoap.store.util.BsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CustomerServ implements CustomerServInt{
    private final CustomerRepoI customerRepo;
    private final EntityConverter<Customer, UserDto> converter;

    @Override
    public List<UserDto> findAllCustomers() {
        var customers = customerRepo.findAll();
        return customers.stream().map(this::toDtoUser).toList();
    }

    private UserDto toDtoUser(Customer customer) {
        UserDto dtoUser = converter.mapEntityToDto(customer, UserDto.class);

        dtoUser.setAddDate(BsUtils.getLocalDateTimeStr(customer.getAddDate()));
        dtoUser.setRecipientSet(customer.getRecipient() != null);

        return dtoUser;
    }

    @Override
    public Customer add(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public SearchResult<UserDto> getCustomerPage(String email,
                                                 Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customerPage = customerRepo.getOnePage(email, pageable);
        Page<UserDto> userDtoPage = customerPage.map(this::toDtoUser);
        int totalPages = customerPage.getTotalPages();
        List<Integer> pageNumbers = null;

        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        var result = new SearchResult<UserDto>(userDtoPage,
                userDtoPage.getNumber() + 1,
                size,
                totalPages,
                pageNumbers
        );
        return result;
    }
}
