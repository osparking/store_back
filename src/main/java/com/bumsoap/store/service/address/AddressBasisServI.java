package com.bumsoap.store.service.address;

import com.bumsoap.store.model.AddressBasis;

public interface AddressBasisServI {
  AddressBasis saveUpdate(AddressBasis addressBasis);
  AddressBasis findByRoadAddress(String roadAddress);
  AddressBasis addGetAddrBasis(AddressBasis addressBasis);
}
