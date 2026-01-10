package com.bumsoap.store.question;

import com.bumsoap.store.model.Question;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bumsoap.store.dto.ReviewRow.formatKoreanDateTime;

@Data
@NoArgsConstructor
public class QuestionRow {
    private long id;
    private long userId;
    private String title;
    private String question;
    private String insertTime;
    private String updateTime;
    private String customerEmail;

    public QuestionRow(Question question) {
        this.id = question.getId();
        this.userId = question.getUser().getId();
        this.title = question.getTitle();
        this.question = question.getQuestion();
        this.insertTime = formatKoreanDateTime(question.getInsertTime());
        this.updateTime = formatKoreanDateTime(question.getUpdateTime());
        this.customerEmail = question.getUser().getEmail();
    }
}
