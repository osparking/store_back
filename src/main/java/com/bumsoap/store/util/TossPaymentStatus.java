package com.bumsoap.store.util;

public enum TossPaymentStatus {
    READY,  // 결제 생성 직 후. 인증되기 전.
    IN_PROGRESS, // 소유자의 결제 수단임이 인증된 상태.
    WAITING_FOR_DEPOSIT,  // 가상계좌에 국한됨. 구매자가 미 입금 상태.
    DONE, // 인증 및 결제 승인 완료 상태.
    CANCELLED,  // 승인된 결제가 취소된 상태.
    PARTIAL_CANCELLED,  // 승인된 결제의 일부가 취소된 상태.
    ABORTED, // 결제 승인에 실패한 상태.
    EXPIRED // IN_PROGRESS 상태에서 30분 이상 경과한 상태
}