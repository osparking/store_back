package com.bumsoap.store.service.soap;

import com.bumsoap.store.dto.ShapeSelDto;
import com.bumsoap.store.dto.ShapeSelItem;
import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.repository.SoapInvenI;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.PriceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvenServ implements InvenServI {
  private final SoapInvenI soapInvenI;
  private final PriceProvider priceProvider;

  @Override
  public SoapInven add(SoapInven soapInven) {
    return soapInvenI.save(soapInven);
  }

  // Helper method to create ShapeSelItem
  private ShapeSelItem createShapeItem(SoapInven inventory,
                                       boolean isOutOfStock) {
    return new ShapeSelItem(
        inventory.getBsShape().label + (isOutOfStock ? "(품절)" : ""),
        inventory.getStockLevel(),
        priceProvider.getShapePrice(inventory.getBsShape().ordinal())
    );
  }

  @Override
  public ShapeSelDto getShapeSelItems() {
    List<SoapInven> soapInventories = soapInvenI.findAll();
    List<ShapeSelItem> inStockList = new ArrayList<>();
    List<SoapInven> outOfStockList = new ArrayList<>();

    soapInventories.forEach(inventory -> {
      if (inventory.getStockLevel() > 0) {
        inStockList.add(createShapeItem(inventory, false));
      } else {
        outOfStockList.add(inventory);
      }
    });

    // Add out-of-stock items with sold-out label
    outOfStockList.forEach(outOfStockItem -> {
      inStockList.add(createShapeItem(outOfStockItem, true));
    });

    // 외형 선택 항 중, 재고 있는 첫째 항
    var firstInStock = inStockList.stream()
        .filter(item -> item.getCount() > 0).findFirst();

    return new ShapeSelDto(inStockList,
        firstInStock.isPresent() ? firstInStock.get().getShapeLabel() : "");
  }

  @Override
  public SoapInven getByBsShape(BsShape bsShape) {
    return soapInvenI.findByBsShape(bsShape)
        .orElseThrow(() -> new DataNotFoundException(
            Feedback.SHAPE_NOT_FOUND + bsShape.label));
  }
}
