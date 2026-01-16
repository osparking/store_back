package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionSaveReq {
    private Long id;
    private Long userId;
    private String title;
    private String question;
}
