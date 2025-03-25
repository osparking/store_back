package com.bumsoap.store.email;

public class EmailProperties {
    public static final String DEFAULT_HOST = "smtp.gmail.com";
    public static final int DEFAULT_PORT = 587;
    public static final String DEFAULT_SENDER = "jbpark03@gmail.com";
    public static final String DEFAULT_USERNAME = "jbpark03@gmail.com";
    public static final String DEFAULT_PASSWORD
            = System.getenv("bumsoap_app_pwd");
    public static final boolean DEFAULT_AUTH = true;
    public static final boolean DEFAULT_STARTTLS = true;
}
