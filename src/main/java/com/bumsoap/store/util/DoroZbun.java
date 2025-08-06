package com.bumsoap.store.util;

public enum DoroZbun {
  ROAD("도로"),
  ZBUN("지번");

  public final String label;

  public static DoroZbun valueOfLabel(String label) {
    for (DoroZbun e : values()) {
      if (e.label.equals(label)) {
        return e;
      }
    }
    return null;
  }

  private DoroZbun(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
