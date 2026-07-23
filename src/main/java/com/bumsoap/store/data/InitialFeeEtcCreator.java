package com.bumsoap.store.data;

import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.repository.FeeEtcRepo;
import com.bumsoap.store.util.BoxSize;
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
        if (feeEtcRepo.count()==0) {
            // Create initial record of delivery fee and other value.
            feeEtcRepo.save(new FeeEtc(BoxSize.BOX_03,
                    BigDecimal.valueOf(4100),
                    BigDecimal.valueOf(4600),
                    BigDecimal.valueOf(7600),
                    BigDecimal.valueOf(4000),
                    BigDecimal.valueOf(40000)
            ));

            feeEtcRepo.save(new FeeEtc(BoxSize.BOX_12,
                    BigDecimal.valueOf(5100),
                    BigDecimal.valueOf(5600),
                    BigDecimal.valueOf(8600),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            ));
        }
        System.out.println("비누 수량 및 수신처 지역별 배송비 등 삽입됨");
    }
}
