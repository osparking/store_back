package com.bumsoap.store.service.worker;

import com.bumsoap.store.dto.EntityConverter;
import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.WorkerRepoI;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.service.photo.PhotoServInt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerServ implements WorkerServInt {
    private final WorkerRepoI workerRepo;
    private final EntityConverter<Worker, UserDto> entityConverter;
    private final PhotoServInt photoServ;

    private UserDto mapWorkerToDtoUser(Worker worker) {
        UserDto dtoUser = entityConverter.mapEntityToDto(worker, UserDto.class);

        if (worker.getPhoto() != null) {
            try {
                byte[] photoBytes = photoServ.getImageData(worker.getPhoto().getId());
                dtoUser.setPhotoBytes(photoBytes);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return dtoUser;
    }

    @Override
    public List<String> findAllDept() {
        return workerRepo.findAllDept();
    }

    @Override
    public Worker add(Worker worker) {
        return workerRepo.save(worker);
    }
}
