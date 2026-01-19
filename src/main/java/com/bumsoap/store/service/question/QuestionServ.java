package com.bumsoap.store.service.question;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.FollowUp;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.question.QuestionRow;
import com.bumsoap.store.repository.FollowUpRepo;
import com.bumsoap.store.repository.QuestionRepo;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.FollowUpData;
import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class QuestionServ implements QuestionServI {
    private final QuestionRepo questionRepo;
    private final UserRepoI userRepo;
    private final FollowUpRepo followUpRepo;

    @Override
    public void deleteFollowUp(Long followUpId) {
        followUpRepo.deleteById(followUpId);
    }

    @Transactional
    @Override
    public FollowUp handleSaveFollowUp(FollowUpData followUpData) {
        Long questionId = followUpData.getQuestionId();
        var question = questionRepo.findById(questionId).orElseThrow(
                () -> new IdNotFoundEx("질문 ID: " + questionId));

        Long writerId = followUpData.getWriterId();
        var writer = userRepo.findById(writerId).orElseThrow(
                () -> new IdNotFoundEx("유저 ID: " + writerId));

        var followUp = new FollowUp(followUpData, question, writer);

        return followUpRepo.save(followUp);
    }

    @Override
    public SearchResult<QuestionTableRowAdmin> getQuestionsPage(
            Integer page, Integer pageSize, Long userId) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<QuestionTableRowAdmin> questionPage = null;

        if (userId == null) {
            questionPage = questionRepo.listQuestionTableRowForAdmin(pageable);
        } else {
            questionPage = questionRepo.listMyQuestionTableRows(userId, pageable);
        }
        int totalPages = questionPage.getTotalPages();

        List<Integer> pageNumbers = null;

        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new SearchResult<QuestionTableRowAdmin>(questionPage,
                questionPage.getNumber() + 1,
                pageSize,
                totalPages,
                pageNumbers
        );
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

    @Override
    public QuestionRow findById(Long id) {
        var question = questionRepo.findById(id).orElseThrow(() ->
                new DataNotFoundException("부재 질문 ID: " + id));
        return new QuestionRow(question);
    }

    @Override
    public void checkIfFollowUpExists(Long id) {
        followUpRepo.findById(id).orElseThrow(
                () -> new IdNotFoundEx("부재 댓글 ID: " + id));
    }

}
