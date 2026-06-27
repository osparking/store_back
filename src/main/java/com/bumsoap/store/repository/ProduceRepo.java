package com.bumsoap.store.repository;

import com.bumsoap.store.dto.ProducePageRow;
import com.bumsoap.store.model.SoapProduce;
import com.bumsoap.store.row.SoapStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduceRepo extends JpaRepository<SoapProduce, Long> {
    @Query("""
            select new com.bumsoap.store.dto.ProducePageRow(
                sp.id, sp.bsShape, sp.quantity, sp.produceDate,
                bu.id as producerId, bu.fullName as producerName,
                bu2.id as registerId, bu2.fullName as registerName,
                sp.registerTime)
            from SoapProduce sp
            join BsUser bu on bu.id = sp.producerId
            join BsUser bu2 on bu2.id = sp.registerId
            order by sp.produceDate desc, sp.registerTime desc
            """
    )
    Page<ProducePageRow> getSoapProducePage(Pageable pageable);

    @Query(value = """
            select shape,
                CAST((sum(produced) - sum(sold)) AS SIGNED) as soap_stock
            from (
                select sp.bs_shape as shape, sum(sp.quantity) as produced,
                    0 as sold
                from soap_produce sp
                group by sp.bs_shape
                union all
                select oi.shape, 0 as produced, sum(oi.count) as sold
                from order_item oi
                join bs_order bo on bo.id = oi.order_id
                where bo.order_status != 0
                group by oi.shape
            ) t
            group by shape
            """, nativeQuery = true)
    List<SoapStock> getSoapStock();
}
