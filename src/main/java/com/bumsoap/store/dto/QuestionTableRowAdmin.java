package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import static com.bumsoap.store.dto.ReviewRow.formatKoreanDateTime;

@Data
@NoArgsConstructor
public class QuestionTableRowAdmin {
    private String title;
    private String insertTime;
    private String question;
    private String answered;
    private Long lastWriterId;
    private Long followUpId;

    public QuestionTableRowAdmin(String title, Timestamp insertTime,
                                 String question, String answered,
                                 Long lastWriterId, Long followUpId) {
        this.title = title;
        this.insertTime = formatKoreanDateTime(insertTime.toLocalDateTime());
        this.question = question;
        this.answered = answered;
        this.lastWriterId = lastWriterId;
        this.followUpId = followUpId;
    }
}
