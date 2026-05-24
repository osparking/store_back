package com.bumsoap.store.service.address;

import com.bumsoap.store.model.AddressBasis;

public interface AddressBasisServI {
  AddressBasis saveUpdate(AddressBasis addressBasis);
  AddressBasis findByRoadAddress(String roadAddress);

  /**
   * 추가하려는 기초 주소가 이미 DB 에 있으면, 그 것을,
   * 아니면(없으면), 삽입한 뒤 그 반환값을 돌려준다.
   *
   * @param addressBasis 삽입하려는 기초 주소
   * @return (결과적으로) DB에 존재하는 기초 주소
   */
  AddressBasis addGetAddrBasis(AddressBasis addressBasis);
}
