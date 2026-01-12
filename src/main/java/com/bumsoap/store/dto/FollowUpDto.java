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
}
