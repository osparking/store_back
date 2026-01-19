package com.bumsoap.store.model;

import com.bumsoap.store.request.FollowUpData;
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
public class FollowUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private BsUser user;

    @Lob
    private String content; // answer or following question

    @CreationTimestamp
    @Column(name = "insert_time", updatable = false, nullable = false)
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "FollowUp{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public FollowUp(FollowUpData followUp, Question question, BsUser user) {
        this.content = followUp.getContent();
        this.id = followUp.getId();
        this.question = question;
        this.user = user;
    }
}
