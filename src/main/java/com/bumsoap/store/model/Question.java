package com.bumsoap.store.model;

import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Lob
    private String question;

    @CreationTimestamp
    @Column(name = "insert_time", updatable = false, nullable = false)
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private BsUser user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OrderBy("insertTime ASC") // Ensures follow-ups are in chronological order
    private List<FollowUp> followUps = new ArrayList<>();

    /**
     * 제목 및 질문 내용 배정
     * @param request
     */
    public Question(QuestionSaveReq request) {
        this.title = request.getTitle();
        this.question = request.getQuestion();
    }

    // Helper method to add follow-up
    public void addFollowUp(FollowUp followUp) {
        followUps.add(followUp);
        followUp.setQuestion(this);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                ", user=" + user +
                ", followUps=" + followUps +
                '}';
    }
}
