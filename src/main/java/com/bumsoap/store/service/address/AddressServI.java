package com.bumsoap.store.service.address;

import com.bumsoap.store.dto.AddressBasisDto;
import com.bumsoap.store.model.SearchKey;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressServI {
  Page<AddressBasisDto> findPaginated(@Valid SearchKey searchKey,
                                      Pageable pageable);
}
