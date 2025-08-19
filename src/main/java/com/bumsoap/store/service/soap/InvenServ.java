package com.bumsoap.store.service.soap;

import com.bumsoap.store.dto.ShapeSelDto;
import com.bumsoap.store.dto.ShapeSelItem;
import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.repository.SoapInvenI;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvenServ implements InvenServI {
  private final SoapInvenI soapInvenI;

  @Override
  public SoapInven add(SoapInven soapInven) {
    return soapInvenI.save(soapInven);
  }

  @Override
  public ShapeSelDto getShapeSelItems() {
    List<SoapInven> soapInventories = soapInvenI.findAll();
    List<ShapeSelItem> inStockList = new ArrayList<>();
    List<SoapInven> ooStockList = new ArrayList<>();

    for (SoapInven inven : soapInventories) {
      if (inven.getStockLevel() > 0) {
        inStockList.add(new ShapeSelItem(
            inven.getBsShape().label, inven.getStockLevel()));
      } else {
        ooStockList.add(inven);
      }
    }

    for (SoapInven inven : ooStockList) {
      inStockList.add(new ShapeSelItem(
          inven.getBsShape().label + "(품절)", 0));
    }

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
