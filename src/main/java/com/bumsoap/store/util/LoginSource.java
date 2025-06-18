package com.bumsoap.store.util;

public enum LoginSource {
  EMAIL("이메일"),
  GOOGLE("구글"),
  NAVER("네이버");

  private final String label;

  LoginSource(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
