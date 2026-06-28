package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.row.SoapPriceRow;
import com.bumsoap.store.util.BsShape;

import java.math.BigDecimal;
import java.util.List;

public interface PriceServI {
  BigDecimal findSoapPrice(BsShape bsShape);

  List<SoapPriceRow> findSoapPrices();

  SoapPrice add(SoapPrice soapInven);
}
