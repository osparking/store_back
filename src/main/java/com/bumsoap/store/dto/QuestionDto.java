package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionDto {
    @Autowired
    private long id;
    private String title;
    private String question;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
}
