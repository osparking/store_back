package com.bumsoap.store.service.question;

import com.bumsoap.store.repository.QuestionRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionServTest {

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuestionServI questionServ;

    @Test
    void findWriterNameByQuestionId() {
        var question = questionRepo.findById(1L);
        assert question.isEmpty() ||
                (!question.get().getUser().getFullName().isEmpty());
    }

    @Test
    void findWriterNameByQuestionIdServ() {
        var writerName = questionServ.findWriterNameByQuestionId(1L);
        System.out.println(writerName);
        assert (!writerName.isEmpty());
    }
}