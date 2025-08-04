package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.util.BsShape;

import java.math.BigDecimal;

public interface PriceServI {
  BigDecimal findSoapPrice(BsShape bsShape);

  SoapPrice add(SoapPrice soapInven);
}
