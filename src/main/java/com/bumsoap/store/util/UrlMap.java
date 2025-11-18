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
    public static final String GET_RECIPIENT = "/{id}/get_recipient";
    public static final String GET_RECIPIENTS = "/get_recipients";
    public static final String CHANGE_PASSWORD = "/change_pwd/{id}";
    public static final String GET_MAX_SUFFIX = "/get_dummy_suffix";
    public static final String GET_DETAILS = "/get_details";
    /*== USER / 유저 끝 ==================================*/

    /*== ADMIN / 관리자 시작 ====================================================*/
    public static final String ADMIN = API + "/admin";
    public static final String USER_COUNT = "/user/count";
    public static final String USER_COUNT_STAT = "/user/count_stat";
    public static final String GET_ALL_WORKERS = "/worker/get_all";
    public static final String GET_ALL_CUSTOMERS = "/customer/get_all";
    public static final String TOGGLE_ENABLED = "/worker/{id}/toggle" ;
    public static final String ADD_PRICE = "/add_price";
    public static final String ADD_FEE_ETC = "/add_fee_etc";
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

    /*== 재료 구매 기록 시작 ====================================== */
    public static final String STORE_INGRED = API + "/store_ingred";
    public static final String GET_INGRE_NAMES = "/get_all_names";
    public static final String GET_BUY_PLACES = "/get_buy_places";
    public static final String GET_PACKUNITS = "/get_packunits";
    /*== 재료 구매 기록 끝 ====================================== */

    public static final String SOAP = API + "/soap";
    public static final String SOAP_PRICE = "/price";
    public static final String SHAPE_PRICE = "/{shape}/price";
    public static final String SOAP_SHAPES = "/shapes";

    /*== 주문-배송 주소 관련 항목 시작 ================================= */
    public static final String ORDER = API + "/order";
    public static final String RECIPIENT = API + "/order/recipient";
    public static final String MY_ROWS = "/myrows";
    public static final String ADD_BASIC_ADDR = "/address/basic/add";
    public static final String ADDRESS_SEARCH = "/address/search";
    public static final String SAVE_RECIPIENT = "/save_recipient";
    public static final String UPDATE2 = "/update";
    public static final String ADD_ORDER_ITEM = "/item/add";

    /*== 카트 관련 항목 시작 ================================= */
    public static final String CART = API + "/cart";
    public static final String ADD_CART_ITEM = "/item/add";
    public static final String GET_BY_USERID = "/{uid}/get";
    public static final String CART_ITEM_COUNT =
        "/{itemId}/{count}/fix";

    /*== 배송비 등 관련 항목 ======== */
    public static final String FEE_ETC = API + "/fee_etc";
    public static final String GET_LATEST = "/get_latest";
    public static final String GET_DELIVERY_FEE = "/get_delivery_fee";

    /*== 결제 관련 항목 ======*/
    public static final String PAYMENTS = API + "/payments";
}
