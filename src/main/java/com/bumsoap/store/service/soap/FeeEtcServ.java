package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.repository.FeeEtcRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeEtcServ implements FeeEtcServI{
  private final FeeEtcRepo feeEtcRepo;

  @Override
  public FeeEtc add(FeeEtc feeEtc) {
    return feeEtcRepo.save(feeEtc);
  }

  @Override
  public FeeEtc readLatest() {
    return feeEtcRepo.findLatestFee();
  }
}
