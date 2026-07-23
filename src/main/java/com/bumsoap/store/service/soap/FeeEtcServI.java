package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.util.BoxSize;

public interface FeeEtcServI {
  FeeEtc add(FeeEtc soapInven);

  FeeEtc getDeliveryFeeOf(BoxSize size);
}
