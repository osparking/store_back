package com.bumsoap.store.security;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCache {
    // 토큰 -> 이메일, 만료 시간(epoch ms)
    private final ConcurrentHashMap<String, CacheEntry> store
            = new ConcurrentHashMap<>();

    private static class CacheEntry {
        String email;
        long expiryMs;

        CacheEntry(String email, long expiryMs) {
            this.email = email;
            this.expiryMs = expiryMs;
        }
    }

    public String put(String email, long ttlSeconds) {
        String token = java.util.UUID.randomUUID().toString();
        long expiryMs = System.currentTimeMillis() +
                TimeUnit.SECONDS.toMillis(ttlSeconds);
        store.put(token, new CacheEntry(email, expiryMs));
        return token;
    }

    public String getAndRemove(String token) {
        var entry = store.get(token);

        if (entry==null || entry.expiryMs < System.currentTimeMillis()) {
            return null; // 만료 또는 없음
        } else {
            return entry.email;
        }
    }

    // (선택) 정리용 스케줄러 - @Scheduled로 주기적 실행 가능
    public void cleanExpired() {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(e -> e.getValue().expiryMs < now);
    }
}