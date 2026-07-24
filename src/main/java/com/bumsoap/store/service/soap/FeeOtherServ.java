package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.FeeOther;
import com.bumsoap.store.repository.FeeOtherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeOtherServ implements FeeOtherServI {
  private final FeeOtherRepo feeOtherRepo;

  @Override
  public FeeOther add(FeeOther feeOther) {
    return feeOtherRepo.save(feeOther);
  }

  @Override
  public FeeOther getLatestFeeOther() {
    return feeOtherRepo.findLatestFeeOther();
  }
}
