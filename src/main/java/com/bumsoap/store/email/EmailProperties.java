package com.bumsoap.store.email;

import lombok.Data;

//@Data
public class EmailProperties {
    public static final String DEFAULT_HOST = "smtp.gmail.com";
    public static final int DEFAULT_PORT = 587;
    public static final String DEFAULT_SENDER = "jbpark03@gmail.com";
    public static final String DEFAULT_USERNAME = "jbpark03@gmail.com";
    public static final String DEFAULT_PASSWORD
            = System.getenv("GMAIL_APP_PWD");
    public static final boolean DEFAULT_AUTH = true;
    public static final boolean DEFAULT_STARTTLS = true;
}
