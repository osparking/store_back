package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.FeeDelivery;
import com.bumsoap.store.util.BoxSize;

public interface FeeDeliveryServI {
  FeeDelivery add(FeeDelivery soapInven);

  FeeDelivery getDeliveryFeeOf(BoxSize size);
}
