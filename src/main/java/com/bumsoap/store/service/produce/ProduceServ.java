package com.bumsoap.store.service.produce;

import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.repository.ProduceRepo;
import com.bumsoap.store.request.AddProduceReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProduceServ implements ProduceServI {
    private final ProduceRepo produceRepo;

    @Override
    public SoapProduce addProduce(
            Long registerWorkerId, AddProduceReq request) {
        var soapProduce = new SoapProduce(request);

        soapProduce.setRegisterId(registerWorkerId);
        return produceRepo.save(soapProduce);
    }
}
