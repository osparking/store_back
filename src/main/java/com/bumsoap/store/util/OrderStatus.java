package com.bumsoap.store.util;

public enum OrderStatus {
  ORDER_PAID("주문"),
  SELLER_RECOGNIZED("판매자 인지"),
  AT_GS25("GS25 접수"),
  COLLECTED("배송기사수거"),
  IN_DELIVERY("배송 중"),
  DELIVERED("배송 완료"),
  PURCHASE_CONFIRMED("구매 확정"),
  CANCELLED("취소됨"),
  REFUNDED("환불됨");

  public final String label;

  public static OrderStatus valueOfLabel(String label) {
    for (OrderStatus e : values()) {
      if (e.label.equals(label)) {
        return e;
      }
    }
    return null;
  }

  private OrderStatus(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
