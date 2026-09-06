package com.bumsoap.store.service.worker;

import com.bumsoap.store.dto.DeletedWorkerInfoDto;
import com.bumsoap.store.dto.EntityConverter;
import com.bumsoap.store.dto.PeopleByDept;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.model.Worker;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.repository.WorkerRepoI;
import com.bumsoap.store.service.photo.PhotoServInt;
import com.bumsoap.store.util.BsUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.bumsoap.store.dto.ReviewRow.formatKoreanDateTime;

@Service
@RequiredArgsConstructor
public class WorkerServ implements WorkerServInt {
    private final WorkerRepoI workerRepo;
    private final EntityConverter<Worker, UserDto> entityConverter;
    private final PhotoServInt photoServ;
    private final EmailManager emailManager;
    private final UserRepoI userRepoI;

    @Override
    public List<UserDto> findAllWorkers() {
        var workers = workerRepo.findAllByOrderByFullNameAsc();
        return workers.stream().map(this::mapWorkerToDtoUser).toList();
    }

    private UserDto mapWorkerToDtoUser(Worker worker) {
        UserDto dtoUser = entityConverter.mapEntityToDto(worker, UserDto.class);

        dtoUser.setAddDate(BsUtils.getLocalDateTimeStr(worker.getAddDate()));
        if (worker.getPhoto()!=null) {
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
    public List<PeopleByDept> employeesByDept() {
        return workerRepo.findPeopleByDept();
    }

    @Override
    public int updateDeptById(Long id, String dept) {
        return workerRepo.updateDeptById(id, dept);
    }

    private void emailDeleted(DeletedWorkerInfoDto workerInfo)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "계정 삭제 안내";
        String EMAIL_TEMPLATE = """
                <p>%s님, 안녕하세요?</p>
                <p>귀하의 범이비누 계정은 다음과 같이 삭제되었습니다.<br />
                이 점을 주지하시고, 필요하면 관리자와 접촉하시기 바랍니다.</p>
                <ul>
                    <li>삭제자: 범이비누 관리자</li>
                    <li>삭제 일시: %s</li>
                    <li>계정 회복 방법: 관리자에 의한 범이비누 계정 수정</li>
                </ul>
                <p>감사합니다.</p>
                - 범이비누 계정 관리""";

        String content = String.format(EMAIL_TEMPLATE,
                workerInfo.getFullName(),
                formatKoreanDateTime(LocalDateTime.now()));

        emailManager.sendMail(workerInfo.getEmail(), subject, "범이비누", content);
    }

    @Override
    public int setWorkerDeleted(long id) {
        try {
            int deletedCount = workerRepo.softDeleteWorkerById(id);
            var workerInfo = userRepoI.findNameEmailById(id).orElseThrow(
                    () -> new RuntimeException("존재하지 않는 직원 ID: " + id));

            emailDeleted(workerInfo);

            return deletedCount;
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Worker add(Worker worker) {
        return workerRepo.save(worker);
    }

    @Override
    public Boolean isAccountDeleted(String email) {
        return workerRepo.isAccountDeleted(email).orElse(false);
    }
}
