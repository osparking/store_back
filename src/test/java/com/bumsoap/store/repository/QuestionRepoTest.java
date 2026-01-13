package com.bumsoap.store.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void listQuestionTableRowForAdmin() {
        int currentPage = 1;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        var list = questionRepo.listQuestionTableRowForAdmin(pageable);
        assertEquals(4, list.getSize());
    }

    @Test
    void listMyQuestionTableRows() {
        int currentPage = 1;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        var rows = questionRepo.listMyQuestionTableRows(2L, pageable);
        assertEquals(3, ((PageImpl) rows).getContent().size());
    }
}