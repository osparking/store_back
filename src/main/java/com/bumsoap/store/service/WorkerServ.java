package com.bumsoap.store.service;

import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.WorkerRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkerServ {
    private final WorkerRepoI workerRepo;
    public Worker add(Worker worker) {
        return workerRepo.save(worker);
    }
}
