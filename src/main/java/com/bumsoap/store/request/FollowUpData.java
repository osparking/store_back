package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowUpData {
  private String content;
  private Long questionId;
  private Long writerId;
}
