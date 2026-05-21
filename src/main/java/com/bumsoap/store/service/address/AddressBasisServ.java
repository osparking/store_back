package com.bumsoap.store.service.address;

import com.bumsoap.store.model.AddressBasis;
import com.bumsoap.store.repository.AddressBasisRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressBasisServ implements AddressBasisServI {
    private final AddressBasisRepoI addrBasisRepo;

    @Override
    public AddressBasis saveUpdate(AddressBasis addressBasis) {
        return addrBasisRepo.save(addressBasis);
    }

    @Override
    public AddressBasis findByRoadAddress(String roadAddress) {
        return addrBasisRepo.findByRoadAddress(roadAddress).orElse(null);
    }

    /**
     * 추가하려는 기초 주소가 이미 DB 에 있으면, 그 것을,
     * 아니면(없으면), 삽입한 뒤 그 반환값을 돌려준다.
     *
     * @param addressBasis 삽입하려는 기초 주소
     * @return (결과적으로) DB에 존재하는 기초 주소
     */
    @Override
    public AddressBasis addGetAddrBasis(AddressBasis addressBasis) {
        // 1. 먼저 조회
        Optional<AddressBasis> existing =
                addrBasisRepo.findByRoadAddress(addressBasis.getRoadAddress());
        if (existing.isPresent()) {
            return existing.get();
        }

        // 2. 저장 시도
        try {
            return addrBasisRepo.save(addressBasis);
        } catch (DataIntegrityViolationException e) {
            // 3. 동시성 충돌 시 재조회
            return addrBasisRepo.findByRoadAddress(addressBasis.getRoadAddress())
                    .orElseThrow(() -> new RuntimeException(
                            "기초주소 저장 혹은 읽는 오류:", e));
        }
    }
}
