package com.bumsoap.store.service.photo;

import com.bumsoap.store.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface PhotoServInt {
    Photo save(Long userId, MultipartFile photo) throws IOException, SQLException;
    Photo findById(Long id);
    void deleteById(Long id);
    Photo update(Long id, MultipartFile file) throws SQLException, IOException;

    byte[] getImageData(Long id);

}
