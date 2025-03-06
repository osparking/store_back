package com.bumsoap.store.repository;

import com.bumsoap.store.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepoI extends JpaRepository<Worker, Long> {
    @Query(nativeQuery = true,
            value = "select distinct w.dept from worker w")
    List<String> findAllDept();
}
