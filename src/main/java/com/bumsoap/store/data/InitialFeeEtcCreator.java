package com.bumsoap.store.data;

import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.repository.FeeEtcRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InitialFeeEtcCreator implements ApplicationListener<ApplicationReadyEvent> {
  private final FeeEtcRepo feeEtcRepo;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    insertFeeEtcIfNotExists();
  }

  private void insertFeeEtcIfNotExists() {
    // Check if there's no existing inventory for delivery fee, etc.
    if (feeEtcRepo.count() == 0) {
      // Create initial record of delivery fee and other value.
      FeeEtc feeEtc = new FeeEtc(
          BigDecimal.valueOf(3400),
          BigDecimal.valueOf(3000),
          BigDecimal.valueOf(5000),
          BigDecimal.valueOf(40000)
      );

      // Save the new SoapInven object to the inventory
      feeEtcRepo.save(feeEtc);
    }
    System.out.println("기본 배송비 등 다양한 요금/기준 삽입됨");
  }
}
