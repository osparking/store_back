package com.bumsoap.store.service.photo;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Employee;
import com.bumsoap.store.model.Photo;
import com.bumsoap.store.repository.EmployeeRepoI;
import com.bumsoap.store.repository.PhotoRepoI;
import com.bumsoap.store.util.Feedback;
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

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(Feedback.NO_PHOTO_SUBMITTED);
        } else {
            Photo photo = new Photo();

            photo.setImage(new SerialBlob(file.getBytes()));
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
            Photo savedPhoto = photoRepo.save(photo);
            Optional<Employee> employee = employeeRepo.findById(empId);
            employee.ifPresentOrElse(emp -> {
                        emp.setPhoto(savedPhoto);
                    },
                    () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + empId));
            employeeRepo.save(employee.get());
            return savedPhoto;
        }
    }

    @Override
    public Photo findById(Long id) {
        return photoRepo.findById(id).orElseThrow(
                () -> new IdNotFoundEx(Feedback.PHOTO_ID_NOT_FOUND + id));
    }

    @Override
    public void deleteById(Long id) {
        photoRepo.deleteById(id);
    }

    @Override
    public Photo update(Long id, MultipartFile file) throws
            SQLException, IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(Feedback.NO_PHOTO_SUBMITTED);
        } else {
            Photo photo = findById(id);

            photo.setImage(new SerialBlob(file.getBytes()));
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
            Photo savedPhoto = photoRepo.save(photo);
            return savedPhoto;
        }
    }

    @Override
    public byte[] getImageData(Long id) throws SQLException {
        try {
            Photo thePhoto = findById(id);
            if (thePhoto == null) {
                return new byte[0];
            } else {
                return thePhoto.getImage().getBytes(1,
                        (int) thePhoto.getImage().length());
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
