package com.bumsoap.store.service.store;

import com.bumsoap.store.dto.*;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.StoreIngre;
import com.bumsoap.store.repository.StoreIngreRepoI;
import com.bumsoap.store.util.BsParameters;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StoreIngreServ implements StoreIngreServI {
  private final StoreIngreRepoI storeIngreRepo;

  @Override
  public StoreIngre findById(Long id) {
    return storeIngreRepo.findById(id).orElseThrow(() ->
        new IdNotFoundEx(Feedback.ST_RE_ID_NOT_FOUND + id));
  }

  @Override
  public String deleteById(Long id) {
    var storedIngre = storeIngreRepo.findById(id);
    if (storedIngre.isPresent()) {
      var storedIngreIns = storedIngre.get();

      storeIngreRepo.delete(storedIngreIns);
      return storedIngreIns.getIngreName();
    } else {
      throw new IdNotFoundEx(Feedback.ST_RE_ID_NOT_FOUND + id);
    }
  }

  @Autowired
  private BsParameters provider;

  private final EntityConverter<StoreIngreRow, StoreIngreDto> stInConverter;

  @Override
  public SearchResult<StoreIngreDto> getIngredientPage(String name,
                                  Optional<Integer> page,
                                  Optional<Integer> size) {
    int pageSize = size.orElse(provider.getPageSize());
    Pageable pageable = PageRequest.of(page.orElse(1) - 1, pageSize);
    Page<StoreIngreRow> ingredientPage = null;

    if (name == null || "".equals(name)) {
      ingredientPage = storeIngreRepo.findPageAll(pageable);
    } else {
      ingredientPage = storeIngreRepo.findPageByName(name, pageable);
    }
    Page<StoreIngreDto> ingredientDtos = ingredientPage.map(row
            -> stInConverter.mapEntityToDto(row, StoreIngreDto.class));
    int totalPages = ingredientPage.getTotalPages();
    List<Integer> pageNumbers = null;

    if (totalPages > 0) {
      pageNumbers = IntStream.rangeClosed(1, totalPages)
              .boxed()
              .collect(Collectors.toList());
    }

    var result = new SearchResult<StoreIngreDto>(ingredientDtos,
            ingredientPage.getNumber() + 1,
            pageSize,
            totalPages,
            pageNumbers
    );
    return result;
  }

}
