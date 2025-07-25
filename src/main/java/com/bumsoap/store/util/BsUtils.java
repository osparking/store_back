package com.bumsoap.store.util;

import com.bumsoap.store.security.user.BsUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

    /**
     * 현재 로그인한 유저가 특정 유저(ID)의 정보레 접근할 자격이 있는지 판단.
     * @param userId 특정 유저 ID
     * @return 자격 유무 - 참: 자격이 있음. 거짓: 자격이 없음
     */
    public static boolean isQualified(Long userId, boolean isUpdate, UserType type) {
        var authen = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authen.getAuthorities().stream().anyMatch(
                role -> "ROLE_ADMIN".equals(role.toString()));
        Long loginId = ((BsUserDetails)authen.getPrincipal()).getId();

        if (Objects.equals(userId, loginId)) {
            return true;
        } else if (isUpdate) {
            return isAdmin && (type == UserType.WORKER);
        } else {
            return isAdmin;
        }
    }

    /**
     * 한 노동자가 입력한 재료 입고 기록을 현재 로그인한 유저가 갱시할 자격이
     * 되는지 판단.
     *
     * @param writerId 재료 입고 기록을 삽입한 노동자 아이디.
     * @return
     */
    public static boolean isQualified(Long writerId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(
                role -> "ROLE_ADMIN".equals(role.toString()));
        Long loginId = ((BsUserDetails)auth.getPrincipal()).getId();

        return isAdmin || Objects.equals(writerId, loginId);
    }
}
