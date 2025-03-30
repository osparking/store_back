package com.bumsoap.store.util;

public class UrlMap {
    public static final String API = "/api/s1";

    /*== Common / 공통 시작 ==================================================*/
    public static final String ADD = "/add";
    public static final String GET_BY_ID = "/{id}/get";
    public static final String UPDATE = "/{id}/update";
    public static final String DELETE_BY_ID = "/{id}/delete";
    public static final String GET_ALL = "/get_all";
    /*== Common / 공통 끝 ===============================*/

    /*== USER / 유저 시작 ====================================================*/
    public static final String USER = API + "/user";
    public static final String GET_USER_DTO_BY_ID = "/{id}/get_dto";
    public static final String CHANGE_PASSWORD = "/change_pwd/{id}";
    /*== USER / 유저 끝 ==================================*/

    /*== ADMIN / 관리자 시작 ====================================================*/
    public static final String ADMIN = API + "/admin";
    public static final String USER_COUNT = "/user/count";
    /*== ADMIN / 관리자 끝 ==================================*/

    /*== PHOTO / 사진 시작 ====================================================*/
    public static final String PHOTO = API + "/photo";
    public static final String UPLOAD = "/upload";
    public static final String PHOTO_UPDATE = "/{photoId}/update";
    public static final String DELETE_BY_EMP_ID = "/{id}/del_emp_id";
    /*== PHOTO / 사진 끝 ==================================*/

    /*== Worker / 노동자 시작 =================================================*/
    public static final String WORKER = API + "/worker";
    public static final String GET_ALL_DEPT = "/get_all_dept";
    /*== WORKER / 노동자 끝 ================================*/

    /*== Authentication / 인증 시작 =========================================*/
    public static final String AUTHO = API + "/autho";
    public static final String LOGIN = "/login";
    public static final String EMAIL_ADDRESS = "/email_address";
    /*== Authentication / 인증 끝 ================================*/

    /*== JWT Token / 토큰 검증 시작 ===========================================*/
    public static final String VERIFY = API + "/verify";
    public static final String TOKEN = "/token";
    public static final String IS_EXPIRED = "/is_expired";
    public static final String SAVE_TOKEN = "/save_token";
    public static final String GENERATE_NEW_TOKEN = "/new_token";
    public static final String DELETE_TOKEN = "/delete_token";
    /*== JWT Token / 토큰 검증 끝 ==================================*/

    /*== 롤/Role 시작 =======================================================*/
    public static final String ROLE = API + "/role";
    public static final String BY_NAME = "/{name}/by_name";
    /*== 롤/Role 끝 ======================================*/

}
