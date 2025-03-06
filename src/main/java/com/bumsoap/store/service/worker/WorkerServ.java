package com.bumsoap.store.service.worker;

import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.WorkerRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerServ implements WorkerServInt {
    private final WorkerRepoI workerRepo;

    @Override
    public List<String> findAllDept() {
        return workerRepo.findAllDept();
    }

    @Override
    public Worker add(Worker worker) {
        return workerRepo.save(worker);
    }
}
