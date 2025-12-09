package com.bumsoap.store.util;

public class Feedback {

    /********************** 공통 사용 가능 메시지 ********************/
    public static final String FOUND = "자원 검색 성공";
    public static final String NOT_FOUND = "자원 검색 실패";

    /********************** 유저 처리 관련 메시지 ********************/
    public static final String USER_ADD_SUCCESS = "유저 등록 성공";
    public static final String USER_TYPE_WRONG = "존재하지 않는 유저 유형";
    public static final String USER_TAKEN_EMAIL = "이미 사용 중인 이메일:";
    public static final String USER_ID_NOT_FOUND = "발견되지 않은 유저 아이디:";
    public static final String USER_UPDATE_SUCCESS = "유저 갱신 성공 - [닫기]로 마감하세요.";
    public static final String DELETEED_USER_NAME = "성공적으로 삭제된 유저 성명: ";
    public static final String USER_DTO_BY_ID = "ID로 유저 찾기 성공";
    public static final String DEPTS_FOUND = "소속 목록 조회됨";
    public static final String DEPTS_READ_FAILURE = "소속 목록 채취 오류";
    public static final String SOME_FIELD_MISSING = "제출 정보 부족 오류";
    public static final String CUR_PASSWORD_WRONG = "기존 비밀번호 불일치";
    public static final String PASSWORD_CHANGED = "비밀번호 변경 완료";
    public static final String NOT_QUALIFIED_FOR = "당신에게 접근권없는 유저ID:";
    public static final String MY_RECIPIENTS_FOUND = "내 수신처 페이지 발견됨";

    /********************** 사진 처리 관련 메시지 ********************/
    public static final String PHOTO_UPLOAD_OK = "사진 올리기 성공";
    public static final String PHOTO_ID_NOT_FOUND = "발견되지 않은 사진 아이디:";
    public static final String PHOTO_FOUND = "사진 ID로 찾기 성공";
    public static final String PHOTO_ERROR = "사진 처리 오류:";
    public static final String PHOTO_DELETE_SUCCESS = "사진 삭제 성공";
    public static final String PHOTO_NOT_FOUND = "사진 발견되지 않음";
    public static final String PHOTO_UPDATE_SUCCESS = "사진 갱신 성공";

    /*************** 계정 관련 메시지 ********************************/
    public static final String CNF_PASSWORD_WRONG = "새 비밀번호 확인 불일치";
    public static final String NO_PHOTO_SUBMITTED = "사진 제출 누락 오류";
    public static final String NOT_FOUND_EMAIL = "존재하지 않는 이메일:";
    public static final String LOGIN_SUCCESS = "로그인 인증 성공";
    public static final String BAD_CREDENTIAL = "자격정보 불일치";
    public static final String LOGIN_FAILURE = "로그인 인증 예외:";
    public static final String AUTHEN_SUCCESS = "계정 인증에 성공하였습니다.";
    public static final String DISABLED_ACCOUNT = "사용 중지된 계정입니다.";

    /*************** 토큰 관련 메시지 ********************************/
    public static final String JWT_WRONG = "잘못된 토큰:";
    public static final String TOKEN_EXPIRED = "만료된 토큰:";
    public static final String TOKEN_IS_VALID = "합법적인 토큰:";
    public static final String TOKEN_SAVED = "유저 토큰 저장됨";
    public static final String TOKEN_DELETED = "유저 토큰 삭제됨";
    public static final String TOKEN_VALI_ERROR = "토큰 검증 오류";
    public static final String ENABLED_TOGGLED = "유저 활성화 토글" ;
    public static final String ENABLED_TOGGLING_ERROR = "유저 활성화 토글 오류";
    public static final String PLZ_VERIFY_EMAIL = "이메일 검증을 진행하십시오.";
    public static final String TRY_SOCIAL_LOGIN = " 계정으로 로그인하세요";

    /** 2FA 관련 메시지 **/
    public static final String DISABLED_2FA = "2FA 비활성화됨";
    public static final String TWO_FA_VERIFIED = "2FA 검증됨";
    public static final String TWO_FA_CODE_ERROR = "2FA 코드 오류";

    /*************** 재료 입고 관련 메시지 ********************************/
    public static final String INGRE_STORE_SUCC = "재료 입고 정보 저장됨";
    public static final String DELETEED_STORE_INGRE = "삭제된 입고 재료명: ";
    public static final String ST_RE_ID_NOT_FOUND = "없는 입고 재료 아이디: ";
    public static final String INGRE_UPDATE_SUCC = "재료 입고 정보 수정됨";
    public static final String NOT_QUALIFIED_ON = "수정 불허 재료입고기록 ID: ";
    public static final String FOUND_INGRE_NAMES = "재료명 목록 발견됨";
    public static final String FOUND_BUY_PLACES = "재료 구매처 목록 발견됨";
    public static final String FOUND_PACKUNITS = "재료 포장 단위 발견됨";

    /*************** 비누 상품 관련 메시지 ********************************/
    public static final String SHAPE_NOT_FOUND = "재고 수량 부재 비누 종류: ";
    public static final String INVEN_UPDATE_SUCC = "비누 재고 갱신 성공";
    public static final String PRICE_INSERT_SUCC = "비누 가격 등록 성공";

    /*************** 주소 관련 메시지 ********************************/
    public static final String ADDRESS_FOUND = "주소 목록 검색 결과";
    public static final String RECIPIENT_SAVED = "수취인 정보 저장됨";
    public static final String BASIC_ADDR_SAVED = "저장된 기초 주소";
    public static final String RECIPIENT_FOUND = "수취인 정보 찾음";
    public static final String RECIP_ID_NOT_FOUND = "없는 수취인 ID: ";
    public static final String RECIPIENT_UPDATED = "수취인 정보 수정됨";
    public static final String DELETEED_RECI_NAME = "삭제된 수취인명: ";
    public static final String DEFAULT_RECIPIENT = "기본 수신처 발견됨";
    public static final String NO_DEFAULT_RECIPIENT = "기본 수신처 없음";

    /*************** 주문 관련 메시지 ********************************/
    public static final String ORDER_ITEM_SAVED = "주문 항목 저장됨";
    public static final String SOAP_ORDER_SAVED = "비누 주문 저장됨";
    public static final String ORDER_UPDATED = "비누 주문 갱신됨";
    public static final String SHORT_INVENTORY = "주문 수량 - 최소: 1, 최대: ";
    public static final String CART_FOUND = "카트 항목(들) 읽음";
    public static final String CART_FIXED = "카트 비누 수량 변경";
    public static final String NO_CART_ITEM = "없는 카트 항목 ID: ";
    public static final String NOT_MY_CART = "내것 아닌 카트 항목 ID: ";
    public static final String CART_ITEM_DELETED = "카트 항목 삭제됨";
    public static final String ORDER_FOUND = "주문 정보 읽음";
    public static final String ORDER_ID_NOT_FOUND = "없는 주문 ID: ";
    public static final String NOT_BELONG_TO_YOU = "소유하지 않은 주문 ID: ";
    public static final String DELETED_ORDER_ID = "삭제된 주문 ID: ";
    public static final String FEE_ETC_INSERTED = "배송비 등 숫자 삽입됨";
    public static final String DELIVERY_FEE_FOUND = "배송비 계산 성공";
    public static final String MY_ORDERS_FOUND = "내 주문 목록 발견";
    public static final String MY_ORDERS_FAILURE = "주문 목록 검색 실패";
    public static final String ORDER_PAGE_FOUND = "주문 페이지 발견";
    public static final String ORDER_PAGE_FAILURE = "주문 페이지 읽기 실패";
    public static final String ORDER_STATUS_LIST = "주문 상태 라벨 목록";
    public static final String ORDER_STATUS_UPDATED = "주문 상태 갱신됨";
    public static final String REVIEW_UPDATED = "주문 후기 갱신됨";
    public static final String REVIEW_UPDATE_FAILED = "후기 갱신 실패함";
    public static final String WAYBILL_NO_STORED = "운송장번호 저장됨";
    public static final String STATUS_UPDATED_FAILED = "주문 상태 갱신 실패";
    public static final String WAYBILL_NO_STORE_FAILED = "운송장번호 저장 실패";
}
