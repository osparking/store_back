package com.bumsoap.store.util;

public enum PackUnit {
  LITER("리터"),
  ML("ml"),
  CC("cc"),
  KG("kg"),
  GRAM("그램");

  public final String label;

  public static PackUnit valueOfLabel(String label) {
    for (PackUnit e : values()) {
      if (e.label.equals(label)) {
        return e;
      }
    }
    return null;
  }

  private PackUnit(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
