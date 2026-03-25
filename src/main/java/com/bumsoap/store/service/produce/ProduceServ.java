package com.bumsoap.store.service.produce;

import com.bumsoap.store.dto.ProduceDto;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.repository.ProduceRepo;
import com.bumsoap.store.request.AddProduceReq;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bumsoap.store.util.BsUtils.getShortTimeStr;

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

    /**
     * 생성된 비누 외형-수량 정보 레코드를 아이디로 검색하여 삭제
     * @param id : 삭제 대상 비누 생성 레코드 아이디
     * @return : 삭제된 생성 레코드 지역 등록 시간 짧은 형태 문자열
     */
    @Override
    public String deleteById(Long id) {
        var produceToDelete = produceRepo.findById(id);

        if (produceToDelete.isPresent()) {
            var storedProduce = produceToDelete.get();

            produceRepo.delete(storedProduce);
            return getShortTimeStr(storedProduce.getRegisterTime());
        } else {
            throw new IdNotFoundEx(Feedback.ST_RE_ID_NOT_FOUND + id);
        }
    }
}
