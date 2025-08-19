package com.bumsoap.store.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BsShape {
  S_WHITE("백설공주"),
  NORMAL("보통비누"),
  MAEJU_S("메주비누");

  public final String label;

  public static List<String> getKoreanLabels() {
    return Arrays.stream(values())
        .map(shape -> shape.label).collect(Collectors.toList());
  }

  public static BsShape valueOfLabel(String label) {
    for (BsShape e : values()) {
      if (e.label.equals(label)) {
        return e;
      }
    }
    return null;
  }

  private BsShape(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
