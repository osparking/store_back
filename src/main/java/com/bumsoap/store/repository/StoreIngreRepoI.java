package com.bumsoap.store.repository;

import com.bumsoap.store.dto.StoreIngreRow;
import com.bumsoap.store.model.StoreIngre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreIngreRepoI extends JpaRepository<StoreIngre, Long> {
    @Query(nativeQuery = true,
            value = "select distinct si.ingre_name from store_ingre si ")
    List<String> findDistinctIngreNames();

    @Query(nativeQuery = true,
            value = "select distinct si.buy_place from store_ingre si")
    List<String> findDistinctBuyPlaces();

    @Query(nativeQuery = true,
            value = "select si.id, si.ingre_name, si.quantity, si.packunit, "
                    + "si.count, si.store_date, si.add_time, si.buy_place, "
                    + "si.worker_id, si.expire_date, bu.full_name as worker_name "
                    + "from store_ingre si join bs_user bu "
                    + "on si.worker_id = bu.id ")
    List<StoreIngreRow> findAllWorkerName();

    @Query(nativeQuery = true,
            value = """
                    select si.id, si.ingre_name, si.quantity,
                      si.packunit, si.count, si.store_date,
                      si.add_time, si.buy_place, si.worker_id,
                      si.expire_date,
                      bu.full_name as worker_name
                    from store_ingre si
                    join bs_user bu on si.worker_id = bu.id
                    """)
    Page<StoreIngreRow> findPageAll(Pageable pageable);

}
