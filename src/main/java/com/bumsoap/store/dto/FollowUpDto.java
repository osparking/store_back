package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FollowUpDto {
    private long id;
    private long questionId;
    private String questionTitle;
    private String followUpContent;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
    private String senderName;
    /**
     * 댓글 등록 사실 통지 이메일을 수신자(관리자 혹은 질문 작성자) 이메일
     */
    private String receiverEmail;

    /**
     * 댓글 등록 사실 통지 이메일을 수신자(관리자 혹은 질문 작성자) 성명
     */
    private String receiverName;
}
