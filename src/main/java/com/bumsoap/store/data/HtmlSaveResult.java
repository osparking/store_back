package com.bumsoap.store.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HtmlSaveResult {
    boolean isSaved;
    String htmlPromoted;
    String htmlDisplay;
    LocalDateTime reviewTime;

    public HtmlSaveResult(String html) {
        this.htmlPromoted = html;
        this.htmlDisplay = html;
    }
}
