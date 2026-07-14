package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                // 해시 값은 유일해야 함
                @UniqueConstraint(columnNames = "token_hash")
        },
        indexes = {
                // 사용자 조회 성능 향상
                @Index(name = "idx_user_id", columnList = "user_id"),

                // 만료된 토큰 정리(배치) 성능 향상
                @Index(name = "idx_expiry_date", columnList = "expiry_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Refresh Token 소유자 (BsUser와 N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private BsUser user;

    // SHA-256 해시 값 (길이 64자, 유일성 보장)
    @Column(name = "token_hash", nullable = false, length = 64, unique = true)
    private String tokenHash;

    // 토큰 만료 일시 (1주일 후)
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // 토큰 폐기 여부 (로테이션 또는 로그아웃 시 true)
    @Column(name = "is_revoked", nullable = false)
    @Builder.Default
    private boolean revoked = false;

    // 토큰 생성 일시 (자동 입력)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 토큰이 아직 유효한지 확인하는 편의 메서드
     * (만료일이 지나지 않았고, 폐기 상태가 아닌 경우)
     */
    public boolean isValid() {
        return !this.revoked && this.expiryDate.isAfter(LocalDateTime.now());
    }
}