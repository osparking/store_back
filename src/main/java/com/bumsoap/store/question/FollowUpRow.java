package com.bumsoap.store.question;

import com.bumsoap.store.model.FollowUp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowUpRow {
    private long id;
    private String content;

    public FollowUpRow(FollowUp followUp) {
        this.id = followUp.getId();
        this.content = followUp.getContent();
    }
}
