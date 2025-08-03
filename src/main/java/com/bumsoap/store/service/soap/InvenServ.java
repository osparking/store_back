package com.bumsoap.store.service.soap;

import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.repository.SoapInvenI;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvenServ implements InvenServI {
  private final SoapInvenI soapInvenI;

  @Override
  public SoapInven add(SoapInven soapInven) {
    return soapInvenI.save(soapInven);
  }

  @Override
  public SoapInven getByBsShape(BsShape bsShape) {
    return soapInvenI.findByBsShape(bsShape)
        .orElseThrow(() -> new DataNotFoundException(
            Feedback.SHAPE_NOT_FOUND + bsShape.label));
  }
}
