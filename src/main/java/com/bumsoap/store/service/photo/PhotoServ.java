package com.bumsoap.store.service.photo;

import com.bumsoap.store.model.Photo;
import com.bumsoap.store.repository.EmployeeRepoI;
import com.bumsoap.store.repository.PhotoRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServ implements PhotoServInt {
    private final EmployeeRepoI employeeRepo;
    private final PhotoRepoI photoRepo;

    @Override
    public Photo save(Long userId, MultipartFile photo) {
        return null;
    }

    @Override
    public Optional<Photo> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Photo update(Long id, byte[] imageData) {
        return null;
    }

    @Override
    public byte[] getImageData(Long id) {
        return new byte[0];
    }
}
