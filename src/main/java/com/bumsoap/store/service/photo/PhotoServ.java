package com.bumsoap.store.service.photo;

import com.bumsoap.store.model.Employee;
import com.bumsoap.store.model.Photo;
import com.bumsoap.store.repository.EmployeeRepoI;
import com.bumsoap.store.repository.PhotoRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServ implements PhotoServInt {
    private final EmployeeRepoI employeeRepo;
    private final PhotoRepoI photoRepo;

    @Override
    public Photo save(Long empId, MultipartFile file) throws
            IOException, SQLException {
        Optional<Employee> employee = employeeRepo.findById(empId);
        Photo photo = new Photo();

        if (file != null && !file.isEmpty()) {
            photo.setImage(new SerialBlob(file.getBytes()));
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getName());
        }
        Photo savedPhoto = photoRepo.save(photo);

        return savedPhoto;
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
