package com.bumsoap.store.util;

import com.bumsoap.store.security.user.BsUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    /**
     * LocalDateTime 객체로부터 "시 분" 형태의 한국어 문자열을 생성합니다.
     *
     * @param dateTime 변환할 LocalDateTime (null 허용)
     * @return "H시 m분" 형식의 문자열, null 입력 시 빈 문자열 반환
     */
    public static String formatHourMinute(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H시 m분");
        return dateTime.format(formatter);
    }

    public static String getLocalDateTimeStr(LocalDateTime ldTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.KOREAN);

        return ldTime.format(formatter);
    }

    public static String getShortTimeStr(LocalDateTime ldTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

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

    public static String getMoneyString(BigDecimal money) {
        DecimalFormat formatter =
                (DecimalFormat) DecimalFormat.getInstance(Locale.KOREA);

        // Customize the grouping separator (optional,
        // if you want a different one than the locale default)
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(','); // Set the thousands separator
        formatter.setDecimalFormatSymbols(symbols);

        // Set whether grouping (thousands separators) should be used
        formatter.setGroupingUsed(true);

        // Format the BigDecimal to a String
        return formatter.format(money);
    }

    @Value("${auth.refresh.expirationSec}")
    private static int expirationSec;

    /**
     * (JWT) 토큰 리프레시 작업용 리프레시 토큰을 ResponseCookie 로 만든다.
     * @param refreshToken 리스레시 토큰     *
     * @return 생성된 ResponseCookie
     */
    public static ResponseCookie createSaveCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken) // 키 이름
                .httpOnly(true) // JavaScript 접근 차단 (보안 핵심)
                // HTTPS 에서만 전송 (운영 환경 필수, 테스트 시 false 가능)
                .secure(true)
                // 모든 경로에서 쿠키 전송 (refresh 엔드포인트가
                // - /autho/refresh_token 이므로 최소한 해당 경로 포함)
                .path("/")
                .maxAge(expirationSec) // (현장용) 1 주 / (시험용) 1 분
                .sameSite("Strict") // CSRF 방지 (Strict 또는 Lax )
                .build();
    }
}
