package com.bumsoap.store.service.produce;

import com.bumsoap.store.dto.ProduceDto;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.request.AddProduceReq;

public interface ProduceServI {
    SoapProduce addProduce(
            Long registerWorkerId, AddProduceReq request);

    SearchResult<ProduceDto> getProducePage(Integer page,
                                            Integer size);

    String deleteById(Long id);
}
