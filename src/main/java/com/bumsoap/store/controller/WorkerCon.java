package com.bumsoap.store.controller;

import com.bumsoap.store.model.Worker;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.worker.WorkerServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMap.WORKER)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class WorkerCon {
    private final WorkerServInt workerServ;

    @PostMapping("/add")
    public void add(@RequestBody Worker worker) {
        workerServ.add(worker);
    }

    @GetMapping(UrlMap.GET_ALL_DEPT)
    public ResponseEntity<ApiResp> findAllDept() {
        try {
            var depts = workerServ.findAllDept();
            return ResponseEntity.ok(new ApiResp(Feedback.DEPTS_FOUND, depts));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResp(Feedback.DEPTS_READ_FAILURE, null ));
        }
    }
}
