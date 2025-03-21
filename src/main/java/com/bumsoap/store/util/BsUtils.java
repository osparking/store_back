package com.bumsoap.store.util;

import java.util.Calendar;
import java.util.Date;

public class BsUtils {
    private static final int EXPIRE_MIN = 10;

    public static Date getExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRE_MIN);
        return new Date(calendar.getTime().getTime());
    }
}
