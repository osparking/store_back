package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private BsUser user;

    @Lob
    private String content; // answer or following question

    private LocalDateTime insertTime;
    private LocalDateTime updateTime;

    @OneToOne
    @JoinColumn(name = "follow_up_id")
    private FollowUp followUp;
}
