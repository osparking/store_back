package com.bumsoap.store.model;

import com.bumsoap.store.util.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * BumSoap(범이비누) 사용자
 *
 * @author <a href="mailto:jbpark03@gmail.com">박종범
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"bsOrders", "verifinTokens", "roles", "cartItems"})
public class BsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    @Column(name = "mb_phone", length = 11, columnDefinition =
            "VARCHAR(11) NOT NULL CHECK (mb_phone REGEXP '^[0-9]{10,11}$')")
    private String mbPhone; // Mobile Phone
    @NaturalId
    private String email;
    private String password;
    private boolean enabled;

    @CreationTimestamp
    private LocalDateTime addDate; // 유저 생성(=추가) 날짜
    private UserType userType;

    @ManyToOne
    @JoinColumn(name = "recipient",
            foreignKey = @ForeignKey(name = "fk_bsUser_recipient"))
    private Recipient recipient;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns
                    = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns
                    = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            uniqueConstraints
                    = @UniqueConstraint(name = "unique_user_role",
                    columnNames = {"user_id", "role_id"}))
    private Collection<Role> roles = new HashSet<>();
    private String signUpMethod; // 이메일, 구글, 네이버, 등
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<VerifinToken> verifinTokens = new ArrayList<>();
    private boolean twoFaEnabled = false;
    private String twoFaSecret;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BsOrder> bsOrders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems = new ArrayList<>();

    // 리프레시 토큰 목록 (양방향 매핑)
    @OneToMany(
            mappedBy = "user",          // RefreshToken 엔티티의 user 필드와 매핑
            cascade = CascadeType.ALL,  // 사용자 저장/삭제 시 연관된 토큰도 함께 처리
            orphanRemoval = true,       // 사용자 삭제 시 연결된 토큰들 자동 삭제
            fetch = FetchType.LAZY      // 성능 최적화
    )
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    public void addRefreshToken(RefreshToken refreshToken) {
        this.refreshTokens.add(refreshToken);
        refreshToken.setUser(this);  // 연관관계 주인(Setter)에도 반영
    }

    public String addedMonth() {
        int monInt = addDate.getMonthValue();
        int yearInt = addDate.getYear();
        return String.format("%d-%02d월", yearInt, monInt);
    }
}
