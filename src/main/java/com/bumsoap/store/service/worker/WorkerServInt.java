package com.bumsoap.store.service.worker;

import com.bumsoap.store.model.Worker;

import java.util.List;

public interface WorkerServInt {
    List<String> findAllDept();
    Worker add(Worker worker);
}
