package com.bumsoap.store.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepoTest {
    @Autowired
    private QuestionRepo questionRepo;

    @Test
    void findQuestionWithFollowUps() {
        var question = questionRepo.findQuestionWithFollowUps(1L);
        assertTrue(question.isPresent());
        System.out.println(question.get().toString());
    }
}