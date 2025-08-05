package com.bumsoap.store.service.address;

import com.bumsoap.store.model.AddressBasis;
import com.bumsoap.store.repository.AddressBasisRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressBasisServ implements AddressBasisServI{
  private final AddressBasisRepoI addrBasisRepo;

  @Override
  public AddressBasis saveUpdate(AddressBasis addressBasis) {
    return addrBasisRepo.save(addressBasis);
  }

  @Override
  public AddressBasis findByRoadAddress(String roadAddress) {
    return addrBasisRepo.findByRoadAddress(roadAddress).orElse(null);
  }
}
