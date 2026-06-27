package com.bumsoap.store.service.produce;

import com.bumsoap.store.dto.ProduceDto;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.dto.ShapeSelItem;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.repository.ProduceRepo;
import com.bumsoap.store.request.AddProduceReq;
import com.bumsoap.store.row.SoapStock;
import com.bumsoap.store.util.BsParameters;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     *
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

    private final BsParameters priceProvider;

    // Helper method to create ShapeSelItem
    private ShapeSelItem createShapeItem(SoapStock soapStock) {
        return new ShapeSelItem(
                soapStock.getShape().label,
                soapStock.getStock(),
                priceProvider.getShapePrice(soapStock.getShape().ordinal())
        );
    }

    @Override
    public List<ShapeSelItem> getShapeSelItems() {
        List<SoapStock> soapStocks = produceRepo.getSoapStock();
        List<ShapeSelItem> shapeSelItems = new ArrayList<>();
        List<SoapStock> outOfStockList = new ArrayList<>();

        soapStocks.forEach(soapStock -> {
            if (soapStock.getStock() > 0) {
                shapeSelItems.add(createShapeItem(soapStock));
            } else {
                outOfStockList.add(soapStock);
            }
        });

        // Append out-of-stock items at the end of selection list
        outOfStockList.forEach(outOfStockItem -> {
            shapeSelItems.add(createShapeItem(outOfStockItem));
        });

        return shapeSelItems;
    }

    private List<SoapStock> cachedList;
    private Map<BsShape, Long> stockMap;

    @Override
    public Long getStockByShape(BsShape shape) {
        List<SoapStock> currentList = produceRepo.getSoapStock();

        if (cachedList!=currentList) {
            cachedList = currentList;
            stockMap = currentList.stream()
                    .collect(Collectors.toMap(SoapStock::getShape, SoapStock::getStock));
        }

        return stockMap.getOrDefault(shape, 0L);
    }

}
