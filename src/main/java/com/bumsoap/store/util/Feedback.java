package com.bumsoap.store.util;

public class Feedback {
    /********************** 유저 처리 관련 메시지 ********************/
    public static final String USER_ADD_SUCCESS = "유저 등록 성공";
    public static final String USER_TYPE_WRONG = "존재하지 않는 유저 유형";
    public static final String USER_TAKEN_EMAIL = "이미 사용 중인 이메일:";
    public static final String USER_ID_NOT_FOUND = "발견되지 않은 유저 아이디:";
    public static final String USER_UPDATE_SUCCESS = "유저 갱신 성공";
    public static final String USER_DELETE_SUCCESS = "유저 삭제 성공";
    public static final String USER_DTO_BY_ID = "ID로 유저 찾기 성공";
    public static final String DEPTS_FOUND = "소속 목록 조회됨";
    public static final String DEPTS_READ_FAILURE = "소속 목록 채취 오류";
    public static final String SOME_FIELD_MISSING = "제출 정보 부족 오류";
    public static final String CUR_PASSWORD_WRONG = "기존 비밀번호 불일치";
    public static final String PASSWORD_CHANGED = "비밀번호 변경 완료";

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

}
