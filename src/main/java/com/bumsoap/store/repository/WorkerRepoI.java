package com.bumsoap.store.repository;

import com.bumsoap.store.dto.PeopleByDept;
import com.bumsoap.store.model.Worker;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepoI extends JpaRepository<Worker, Long> {
    @Query(nativeQuery = true,
            value = "select distinct w.dept from worker w")
    List<String> findAllDept();

    @Query(nativeQuery = true,
            value = """
                    select dept as department, count(*) as people
                    from worker
                    group by department
                    order by people desc
                    """)
    List<PeopleByDept> findPeopleByDept();

    @Modifying
    @Transactional
    @Query("UPDATE Worker w SET w.dept = :dept WHERE w.id = :id")
    int updateDeptById(@Param("id") long id, @Param("dept") String dept);

    @Modifying
    @Transactional
    @Query("UPDATE Worker w SET w.deleted = true WHERE w.id = :id")
    int softDeleteWorkerById(@Param("id") long id);

    @Query(nativeQuery = true,
            value = """
                    SELECT w.deleted
                    FROM bs_user bu join worker w on w.worker_id = bu.id
                    WHERE bu.email = :email
                    """)
    Optional<Boolean> isAccountDeleted(@Param("email") String email);
}
