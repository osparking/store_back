package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionTableRowAdmin {
    private String title;
    private LocalDateTime insertTime;
    private String question;
    private String answered;
    private Long lastWriterId;
    private Long followUpId;

    public QuestionTableRowAdmin(String title, Timestamp insertTime,
                                 String question, String answered,
                                 Long lastWriterId, Long followUpId) {
        this.title = title;
        this.insertTime = insertTime.toLocalDateTime();
        this.question = question;
        this.answered = answered;
        this.lastWriterId = lastWriterId;
        this.followUpId = followUpId;
    }
}
