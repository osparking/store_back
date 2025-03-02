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
    /*== USER / 유저 끝 ==================================*/

    /*== PHOTO / 사진 시작 ====================================================*/
    public static final String PHOTO = API + "/photo";
    public static final String UPLOAD = "/upload";
    public static final String DELETE_BY_EMP_ID = "/{id}/del_emp_id";
    /*== PHOTO / 사진 끝 ==================================*/
}
