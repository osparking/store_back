package com.bumsoap.store.util;

import com.bumsoap.store.dto.SoapPriceDto;
import com.bumsoap.store.repository.SoapPriceRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@DependsOn({"initialSoapInvenCreator", "initialSoapPriceCreator"})
public class BsParameters {
    private Map<Integer, BigDecimal> priceMap;
    private final SoapPriceRepo soapPriceRepo;

    public BsParameters(SoapPriceRepo soapPriceRepo) {
        this.soapPriceRepo = soapPriceRepo;
    }

    @PostConstruct
    public void init() {
        List<SoapPriceDto> soapPrices = soapPriceRepo.findSoapPrices();
        priceMap = soapPrices.stream()
                .collect(Collectors.toMap(
                        SoapPriceDto::getShapeOrdinal,
                        SoapPriceDto::getUnitPrice
                ));
    }

    public BigDecimal getShapePrice(int ordinal) {
        return priceMap.get(ordinal);
    }

    public int getPageSize() {
        return 5;
    }
}
