package com.bumsoap.store.service.photo;

import com.bumsoap.store.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoServInt {
    Photo save(Long userId, MultipartFile photo);
    Optional<Photo> findById(Long id);
    void deleteById(Long id);
    Photo update(Long id, byte[] imageData);
    byte[] getImageData(Long id);
}
