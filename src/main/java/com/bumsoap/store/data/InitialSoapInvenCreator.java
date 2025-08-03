package com.bumsoap.store.data;

import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.repository.SoapInvenI;
import com.bumsoap.store.util.BsShape;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialSoapInvenCreator implements ApplicationListener<ApplicationReadyEvent> {
    private final SoapInvenI soapInvenI;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        insertSoapInvensIfNotExists();
    }

    private void insertSoapInvensIfNotExists() {
        int insertCount = 0;
        // Check if there's no existing inventory for soap with the NORMAL shape
        if (soapInvenI.findByBsShape(BsShape.NORMAL).isEmpty()) {
            // Create a new SoapInven object for the NORMAL shape with default values
            SoapInven normal = new SoapInven(
                BsShape.NORMAL, // Shape of the soap
                0               // Initial quantity
            );

            // Save the new SoapInven object to the inventory
            soapInvenI.save(normal);
            insertCount++;
        }
        // Check if there's no existing inventory for soap with the MAEJU shape
        if (soapInvenI.findByBsShape(BsShape.MAEJU_S).isEmpty()) {
            // Create a new SoapInven object for the MAEJU shape with default values
            SoapInven normal = new SoapInven(
                BsShape.MAEJU_S, // Shape of the soap
                0                // Initial quantity
            );

            // Save the new SoapInven object to the inventory
            soapInvenI.save(normal);
            insertCount++;
        }
        // Check if there's no existing inventory for soap with the SNOW WHITE shape
        if (soapInvenI.findByBsShape(BsShape.S_WHITE).isEmpty()) {
            // Create a new SoapInven object for the SNOW WHITE shape with default values
            SoapInven normal = new SoapInven(
                BsShape.S_WHITE, // Shape of the soap
                500              // Initial quantity
            );

            // Save the new SoapInven object to the inventory
            soapInvenI.save(normal);
            insertCount++;
        }
        if (insertCount > 0) {
            System.out.println("삽입된 범이비누 재고량 레코드 수: " + insertCount);
        }
    }
}
