package com.bumsoap.store.data;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.repository.SoapPriceRepo;
import com.bumsoap.store.util.BsShape;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InitialSoapPriceCreator {
  private final SoapPriceRepo soapPriceRepo;

  @PostConstruct
  public void insertSoapPriceIfNotExists() {
    int insertCount = 0;
    // Check if there's no existing inventory for soap with the NORMAL shape
    if (soapPriceRepo.countByBsShape(BsShape.NORMAL) == 0) {
      // Create a new SoapInven object for the NORMAL shape with default values
      SoapPrice normal = new SoapPrice(
          BsShape.NORMAL, // Shape of the soap
          BigDecimal.valueOf(4110) // Price
      );

      // Save the new SoapInven object to the inventory
      soapPriceRepo.save(normal);
      insertCount++;
    }
    // Check if there's no existing inventory for soap with the MAEJU shape
    if (soapPriceRepo.countByBsShape(BsShape.MAEJU_S) == 0) {
      // Create a new SoapInven object for the MAEJU shape with default values
      SoapPrice maeju = new SoapPrice(
          BsShape.MAEJU_S, // Shape of the soap
          BigDecimal.valueOf(4020) // Price
      );

      // Save the new SoapInven object to the inventory
      soapPriceRepo.save(maeju);
      insertCount++;
    }
    // Check if there's no existing inventory for soap with the SNOW WHITE shape
    if (soapPriceRepo.countByBsShape(BsShape.S_WHITE) == 0) {
      // Create a new SoapInven object for the SNOW WHITE shape with default values
      SoapPrice white = new SoapPrice(
          BsShape.S_WHITE, // Shape of the soap
          BigDecimal.valueOf(3900) // Price
      );

      // Save the new SoapInven object to the inventory
      soapPriceRepo.save(white);
      insertCount++;
    }
    if (insertCount > 0) {
      System.out.println("삽입된 범이비누 단가 레코드 수: " + insertCount);
    }
  }
}
