package com.bumsoap.store.controller;

import com.bumsoap.store.model.Admin;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.service.worker.WorkerServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(UrlMap.ADMIN)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AdminCon {
    private final AdminServ adminServ;
    private final UserServInt userServ;
    private final WorkerServInt workerServ;

    @PutMapping(UrlMap.TOGGLE_ENABLED)
    public ResponseEntity<ApiResp> toggleEnabledColumn(
            @PathVariable Long id) {
        try {
            int count = userServ.toggleEnabledColumn(id);
            if (count == 1) {
                return ResponseEntity.ok(
                        new ApiResp(Feedback.ENABLED_TOGGLED, null));
            } else {
                throw new RuntimeException(Feedback.ENABLED_TOGGLING_ERROR);
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_ALL_WORKERS)
    public ResponseEntity<ApiResp> findAllWorkers() {
        try {
            var workers = workerServ.findAllWorkers();
            return ResponseEntity.ok(new ApiResp(Feedback.FOUND, workers));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResp(Feedback.NOT_FOUND, null ));
        }
    }

    @PostMapping("/add")
    public void add(@RequestBody Admin admin) {
        adminServ.add(admin);
    }

    @GetMapping(UrlMap.USER_COUNT_STAT)
    public ResponseEntity<ApiResp> countUsersByMonthAndType(){
        try {
            var userStat = userServ.countUsersByMonthAndType();
            return ResponseEntity.status(OK)
                    .body(new ApiResp(Feedback.FOUND, userStat));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.USER_COUNT)
    public long countAllUsers() {
        return userServ.countAll();
    }
}
