package com.bumsoap.store.controller;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.photo.PhotoServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(UrlMap.PHOTO)
@RequiredArgsConstructor
public class PhotoCon {
    private final PhotoServInt photoServ;

    @PostMapping(UrlMap.UPLOAD)
    public ResponseEntity<ApiResp> upload(@RequestParam MultipartFile file,
                                          @RequestParam Long empId) {
        try {
            photoServ.save(empId, file);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.PHOTO_UPLOAD_OK, null));
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (SQLException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }
}
