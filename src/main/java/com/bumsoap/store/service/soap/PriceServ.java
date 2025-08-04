package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.repository.SoapPriceRepo;
import com.bumsoap.store.util.BsShape;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PriceServ implements PriceServI{
  private final SoapPriceRepo soapRepo;

  @Override
  public BigDecimal findSoapPrice(BsShape bsShape) {
    return soapRepo.findSoapPrice(bsShape.ordinal());
  }

  @Override
  public SoapPrice add(SoapPrice soapInven) {
    return soapRepo.save(soapInven);
  }
}
