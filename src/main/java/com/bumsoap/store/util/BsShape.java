package com.bumsoap.store.util;

public enum BsShape {
  NORMAL("보통비누"),
  S_WHITE("백설공주"),
  MAEJU_S("메주비누");

  public final String label;

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
