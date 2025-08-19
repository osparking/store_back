package com.bumsoap.store.service.soap;

import com.bumsoap.store.dto.ShapeSelDto;
import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.util.BsShape;

public interface InvenServI {
  SoapInven add(SoapInven soapInven);

  ShapeSelDto getShapeSelItems();

  SoapInven getByBsShape(BsShape bsShape);
}
