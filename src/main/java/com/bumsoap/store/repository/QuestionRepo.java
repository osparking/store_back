package com.bumsoap.store.repository;

import com.bumsoap.store.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q " +
            "LEFT JOIN FETCH q.followUps f " +
            "WHERE q.id = :questionId " +
            "ORDER BY f.insertTime DESC")
    Optional<Question> findQuestionWithFollowUps(
            @Param("questionId") Long questionId);
}
