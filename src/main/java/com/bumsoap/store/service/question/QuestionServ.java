package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.repository.QuestionRepo;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServ implements QuestionServI {
    private final QuestionRepo questionRepo;
    private final UserRepoI userRepo;

    @Override
    public Page<QuestionTableRowAdmin> getQuestionsPage(Integer page,
                                                        Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return questionRepo.listQuestionTableRowForAdmin(pageable);
    }

    @Transactional
    @Override
    public Question handleSaveQuestion(QuestionSaveReq request) {
        var question = new Question(request);
        var userId = request.getUserId();
        var user = userRepo.findById(userId).orElseThrow(
                () -> new IdNotFoundEx("유저 ID: " + userId));

        question.setUser(user);

        return questionRepo.save(question);
    }

}
