package com.bumsoap.store.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HtmlSaveResult {
    boolean isSaved;
    String htmlPromoted;
}
