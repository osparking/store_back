package com.bumsoap.store.util;

public enum OrderStatus {
  PAY_WAIT("결제대기"),
  PAID("결제완료"),
  SELLER_RECOGNIZED("발주확인"),
  AT_GS25("GS25 접수"),
  COLLECTED("배송기사수거"),
  IN_DELIVERY("배송 중"),
  DELIVERED("배송 완료"),
  RECEIVED("수취 확인"),
  PURCHASE_CONFIRMED("구매 확정"),
  REVIEWED("후기 존재"),
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
