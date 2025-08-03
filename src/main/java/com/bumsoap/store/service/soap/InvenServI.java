package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.util.BsShape;

public interface InvenServI {
  SoapInven add(SoapInven soapInven);

  SoapInven getByBsShape(BsShape bsShape);
}
