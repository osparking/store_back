package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.repository.SoapPriceI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceServ implements PriceServI{
  private final SoapPriceI soapRepo;

  @Override
  public SoapPrice add(SoapPrice soapInven) {
    return soapRepo.save(soapInven);
  }
}
