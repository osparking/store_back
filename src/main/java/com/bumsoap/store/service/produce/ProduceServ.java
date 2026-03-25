package com.bumsoap.store.service.produce;

import com.bumsoap.store.dto.ProduceDto;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.repository.ProduceRepo;
import com.bumsoap.store.request.AddProduceReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Override
    public SearchResult<ProduceDto> getProducePage(
            Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        var produceRowPage = produceRepo.getSoapProducePage(pageable);
        var dtoPage = produceRowPage.map(ProduceDto::new);

        int totalPages = produceRowPage.getTotalPages();
        List<Integer> pageNumbers = null;

        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new SearchResult<>(dtoPage, produceRowPage.getNumber() + 1,
                size, totalPages, pageNumbers
        );
    }

    @Override
    public String deleteById(Long id) {
        return "백설공주";
    }
}
