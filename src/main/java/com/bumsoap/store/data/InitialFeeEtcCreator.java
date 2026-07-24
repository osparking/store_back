package com.bumsoap.store.data;

import com.bumsoap.store.model.FeeDelivery;
import com.bumsoap.store.model.FeeOther;
import com.bumsoap.store.repository.FeeDeliveryRepo;
import com.bumsoap.store.repository.FeeOtherRepo;
import com.bumsoap.store.util.BoxSize;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InitialFeeEtcCreator implements ApplicationListener<ApplicationReadyEvent> {
    private final FeeDeliveryRepo feeEtcRepo;
    private final FeeOtherRepo feeOtherRepo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        insertFeeEtcIfNotExists();
        insertFeeOtherIfNotExists();
    }

    private void insertFeeEtcIfNotExists() {
        // Check if there's no existing inventory for delivery fee, etc.
        if (feeEtcRepo.count()==0) {
            // Create initial record of delivery fee and other value.
            feeEtcRepo.save(new FeeDelivery(BoxSize.BOX_03,
                    BigDecimal.valueOf(4100),
                    BigDecimal.valueOf(4600),
                    BigDecimal.valueOf(7600)
            ));

            feeEtcRepo.save(new FeeDelivery(BoxSize.BOX_12,
                    BigDecimal.valueOf(5100),
                    BigDecimal.valueOf(5600),
                    BigDecimal.valueOf(8600)
            ));
        }
        System.out.println("비누 수량 및 수신처 지역별 배송비 등 삽입됨");
    }

    private void insertFeeOtherIfNotExists() {
        // Check if there's no existing inventory for delivery fee, etc.
        if (feeOtherRepo.count()==0) {
            // 섬 배송비 및 무료 배송 기준 비누 구매 최소액
            feeOtherRepo.save(new FeeOther(
                    BigDecimal.valueOf(4000),
                    BigDecimal.valueOf(40000)
            ));
        }
        System.out.println("제주 외 섬 배송비 등 삽입됨");
    }
}
