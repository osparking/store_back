package com.bumsoap.store.repository;

import com.bumsoap.store.dto.ProducePageRow;
import com.bumsoap.store.model.SoapProduce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduceRepo extends JpaRepository<SoapProduce, Long> {
    @Query("""
            select new com.bumsoap.store.dto.ProducePageRow(
                sp.id, sp.bsShape, sp.quantity, sp.produceDate,
                bu.fullName as producer,
                bu2.fullName as register, sp.registerTime)
            from SoapProduce sp
            join BsUser bu on bu.id = sp.producerId
            join BsUser bu2 on bu2.id = sp.registerId
            order by sp.produceDate desc, sp.registerTime desc
            """
    )
    Page<ProducePageRow> getSoapProducePage(Pageable pageable);
}
