package com.bumsoap.store.service.soap;

import com.bumsoap.store.dto.ShapeSelItem;
import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.util.BsShape;

import java.util.List;

public interface InvenServI {
  SoapInven add(SoapInven soapInven);

  List<ShapeSelItem> getShapeSelItems();

  SoapInven getByBsShape(BsShape bsShape);
}
