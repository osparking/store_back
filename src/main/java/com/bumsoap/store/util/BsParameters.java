package com.bumsoap.store.util;

import com.bumsoap.store.dto.SoapPriceDto;
import com.bumsoap.store.repository.SoapPriceRepo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BsParameters {
  private final Map<Integer, BigDecimal> priceMap;

  public BsParameters(SoapPriceRepo soapPriceRepo) {
    List<SoapPriceDto> soapPrices = soapPriceRepo.findSoapPrices();
    priceMap = soapPrices.stream()
        .collect(Collectors.toMap(
            SoapPriceDto::getShapeOrdinal,
            SoapPriceDto::getUnitPrice
        ));
  }

  public BigDecimal getShapePrice(int ordinal) {
    return priceMap.get(ordinal);
  }

  public int getPageSize() {
    return 5;
  }
}
