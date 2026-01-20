package com.bumsoap.store.repository;

import com.bumsoap.store.dto.QuestionTableRowAdmin;
import com.bumsoap.store.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    final static String SELECT_QUESTION =
            """
                    SELECT
                        q.id,
                        IF(
                            CHAR_LENGTH(REGEXP_REPLACE(q.title, '<[^>]*>', '')) > 18,
                            CONCAT(SUBSTRING(REGEXP_REPLACE(q.title, '<[^>]*>', ''), 1, 15), '...'),
                            REGEXP_REPLACE(q.title, '<[^>]*>', '')
                        ) as title,
                        q.insert_time,
                        IF(
                            CHAR_LENGTH(REGEXP_REPLACE(q.question, '<[^>]*>', '')) > 23,
                            CONCAT(SUBSTRING(REGEXP_REPLACE(q.question, '<[^>]*>', ''), 1, 20), '...'),
                            REGEXP_REPLACE(q.question, '<[^>]*>', '')
                        ) as question,
                       CASE
                         WHEN latest_fu.user_id = 1 THEN '완료'
                         ELSE '대기'
                       END AS answered,
                       latest_fu.user_id as last_writer_id,
                       latest_fu.id as followUpId,
                       latest_fu.fuCount as followUpCount
                    FROM question q
                    LEFT OUTER JOIN (
                        SELECT f1.*, f2.fuCount
                        FROM follow_up f1
                        INNER JOIN (
                            SELECT question_id, MAX(id) as max_id, count(*) as fuCount
                            FROM follow_up
                            GROUP BY question_id
                        ) f2 ON f1.question_id = f2.question_id and
                                f1.id = f2.max_id
                    ) latest_fu ON q.id = latest_fu.question_id
                    """;

    @Query(nativeQuery = true,
            value = SELECT_QUESTION + "order by q.insert_time desc")
    Page<QuestionTableRowAdmin> listQuestionTableRowForAdmin(Pageable pageable);

    @Query(nativeQuery = true,
            value = SELECT_QUESTION +
                    """
                            where q.user_id = :userId
                            order by q.insert_time desc
                            """
    )
    Page<QuestionTableRowAdmin> listMyQuestionTableRows(
            @Param("userId") Long userId,
            Pageable pageable);
}
