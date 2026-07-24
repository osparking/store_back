package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.FeeDelivery;
import com.bumsoap.store.repository.FeeDeliveryRepo;
import com.bumsoap.store.util.BoxSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeDeliveryServ implements FeeDeliveryServI {
  private final FeeDeliveryRepo feeEtcRepo;

  @Override
  public FeeDelivery add(FeeDelivery feeEtc) {
    return feeEtcRepo.save(feeEtc);
  }

  @Override
  public FeeDelivery getDeliveryFeeOf(BoxSize size) {
    return feeEtcRepo.findLatestFee(size);
  }
}
