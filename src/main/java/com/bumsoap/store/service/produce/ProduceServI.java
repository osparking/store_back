package com.bumsoap.store.service.produce;

import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.request.AddProduceReq;

public interface ProduceServI {
    SoapProduce addProduce(
            Long registerWorkerId, AddProduceReq request);
}
