package com.bumsoap.store.service.address;

import com.bumsoap.store.model.AddressDJ;
import com.bumsoap.store.model.SearchKey;
import com.bumsoap.store.util.ZipCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServ implements AddressServI{
  @Override
  public Page<AddressDJ> findPaginated(@Valid SearchKey searchKey,
                                       Pageable pageable) {
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    List<AddressDJ> pageAddresses = new ArrayList<AddressDJ>();
    int[] result = new int[2];

    ZipCode.find(searchKey.getAddrKey(), currentPage + 1, pageSize,
        pageAddresses, result);
    Page<AddressDJ> addressPage = new PageImpl<AddressDJ>(pageAddresses,
        PageRequest.of(currentPage, pageSize), result[0]);

    return addressPage;
  }
}