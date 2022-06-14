package com.aibaixun.uaa.auth;

/**
 * 静态常量
 * @author wangxiao
 */
public interface SecurityConstants {
    String USERNAME_FIELD = "username";

    String PASSWORD_FIELD = "password";

    String MOBILE_FIELD = "mobile";

    String EMAIL_FIELD = "email";

    String TOKEN_FIELD = "uid";

    String TOKEN_PREFIX = "uaa-auth:";


    String MOBILE_PREFIX = TOKEN_PREFIX+"mobile:";

    String ADMIN_TYPE ="admin";


    String SWAGGER_URL = "/v2/api-docs";
    String AUTH_URL= "/uaa/auth";
    String USERNAME_AUTH_URL = AUTH_URL+"/username";
    String MOBILE_AUTH_URL = AUTH_URL+"/mobile";
    String EMAIL_AUTH_URL = AUTH_URL+"/email";
    String REFRESH_AUTH_URL= AUTH_URL+"/refresh";
    String LOGOUT_AUTH_URL= AUTH_URL+"/logout";

    String CHECK_AUTH_URL= AUTH_URL+"/check";

}
