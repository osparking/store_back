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
  private ShapeSelItem createShapeItem(SoapInven inventory) {
    return new ShapeSelItem(
        inventory.getBsShape().label,
        inventory.getStockLevel(),
        priceProvider.getShapePrice(inventory.getBsShape().ordinal())
    );
  }

  @Override
  public ShapeSelDto getShapeSelItems() {
    List<SoapInven> soapInventories = soapInvenI.findAll();
    List<ShapeSelItem> shapeSelItems = new ArrayList<>();
    List<SoapInven> outOfStockList = new ArrayList<>();

    soapInventories.forEach(inventory -> {
      if (inventory.getStockLevel() > 0) {
        shapeSelItems.add(createShapeItem(inventory));
      } else {
        outOfStockList.add(inventory);
      }
    });

    // Append out-of-stock items at the end of selection list
    outOfStockList.forEach(outOfStockItem -> {
      shapeSelItems.add(createShapeItem(outOfStockItem));
    });

    // 외형 선택 항 중, 재고 있는 첫째 항
    var firstInStock = shapeSelItems.stream()
        .filter(item -> item.getCount() > 0).findFirst();

    return new ShapeSelDto(shapeSelItems,
        firstInStock.isPresent() ? firstInStock.get().getShapeLabel() : "");
  }

  @Override
  public SoapInven getByBsShape(BsShape bsShape) {
    return soapInvenI.findByBsShape(bsShape)
        .orElseThrow(() -> new DataNotFoundException(
            Feedback.SHAPE_NOT_FOUND + bsShape.label));
  }
}
