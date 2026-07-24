package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.FeeOther;

public interface FeeOtherServI {
  FeeOther add(FeeOther feeOther);

  FeeOther getLatestFeeOther();
}
