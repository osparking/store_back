package com.bumsoap.store.controller;

import com.bumsoap.store.model.Worker;
import com.bumsoap.store.service.WorkerServ;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerCon {
    private final WorkerServ workerServ;

    @PostMapping("/add")
    public void add(@RequestBody Worker worker) {
        workerServ.add(worker);
    }
}
