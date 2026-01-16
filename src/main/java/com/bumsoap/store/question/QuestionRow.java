package com.bumsoap.store.question;

import com.bumsoap.store.model.Question;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<FollowUpRow> followUpRows;

    public QuestionRow(Question question) {
        this.id = question.getId();
        this.userId = question.getUser().getId();
        this.title = question.getTitle();
        this.question = question.getQuestion();
        this.insertTime = formatKoreanDateTime(question.getInsertTime());
        this.updateTime = formatKoreanDateTime(question.getUpdateTime());
        this.customerEmail = question.getUser().getEmail();
        this.followUpRows = question.getFollowUps().stream()
                .map(FollowUpRow::new).collect(Collectors.toList());
    }
}
