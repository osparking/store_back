package com.bumsoap.store.controller;

import com.bumsoap.store.request.DeptChangeReq;
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
public class WorkerCon {
    private final WorkerServInt workerServ;

    @GetMapping(UrlMap.GET_ALL_DEPT)
    public ResponseEntity<ApiResp> findAllDept() {
        try {
            var depts = workerServ.findAllDept();
            return ResponseEntity.ok(new ApiResp(Feedback.DEPTS_FOUND, depts));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResp(Feedback.DEPTS_READ_FAILURE, null));
        }
    }

    @PutMapping(UrlMap.CHANGE_DEPT)
    public ResponseEntity<ApiResp> update(@PathVariable("id") Long id,
                                          @RequestBody DeptChangeReq req) {
        try {
            int updatedRowCount =
                    workerServ.updateDeptById(id, req.getDept());
            if (updatedRowCount==1) {
                return ResponseEntity.ok(new ApiResp(
                        Feedback.DEPT_UPDATED, req.getDept()));
            } else {
                throw new Exception("직원 소속 갱신 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResp(Feedback.DEPTS_READ_FAILURE, e.getMessage()));
        }
    }
}
