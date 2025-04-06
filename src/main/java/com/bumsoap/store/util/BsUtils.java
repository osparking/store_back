package com.bumsoap.store.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BsUtils {
    private static final int EXPIRE_MIN = 10;

    public static Date getExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRE_MIN);
        return new Date(calendar.getTime().getTime());
    }

    public static String getLocalDateTimeStr(LocalDateTime ldTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.KOREAN);

        return ldTime.format(formatter);
    }
}
