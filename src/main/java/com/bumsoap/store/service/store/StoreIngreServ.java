package com.bumsoap.store.service.store;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.StoreIngre;
import com.bumsoap.store.repository.StoreIngreRepoI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
