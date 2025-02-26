package com.bumsoap.store.repository;

import com.bumsoap.store.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepoI extends JpaRepository<Worker, Long> {
}
