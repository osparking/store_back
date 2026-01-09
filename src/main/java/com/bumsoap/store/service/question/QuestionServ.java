package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.repository.QuestionRepo;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServ implements QuestionServI {
    private final QuestionRepo questionRepo;
    private final UserRepoI userRepo;

    @Override
    public List<QuestionTableRowAdmin> getAllQuestions() {
        return questionRepo.listQuestionTableRowForAdmin();
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
