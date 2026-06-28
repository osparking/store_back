package com.bumsoap.store.service.soap;

import com.bumsoap.store.model.SoapPrice;
import com.bumsoap.store.repository.SoapPriceRepo;
import com.bumsoap.store.row.SoapPriceRow;
import com.bumsoap.store.util.BsShape;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServ implements PriceServI {
    private final SoapPriceRepo soapRepo;

    @Override
    public BigDecimal findSoapPrice(BsShape bsShape) {
        return soapRepo.findSoapPrice(bsShape.ordinal());
    }

    @Override
    public List<SoapPriceRow> findSoapPrices() {
        var priceDtoList = soapRepo.findSoapPrices();

        return priceDtoList.stream()
                .map(SoapPriceRow::new)
                .collect(Collectors.toList());
    }

    @Override
    public SoapPrice add(SoapPrice soapInven) {
        return soapRepo.save(soapInven);
    }
}
