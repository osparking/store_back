package com.bumsoap.store.model;

import com.bumsoap.store.request.QuestionSaveReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "follow_up_id")
    private FollowUp followUp;

    /**
     * 제목 및 질문 내용 배정
     * @param request
     */
    public Question(QuestionSaveReq request) {
        this.title = request.getTitle();
        this.question = request.getQuestion();
    }
}
