package com.bumsoap.store.model;

import com.bumsoap.store.util.UserType;
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
 * @author <a href="mailto:jbpark03@gmail.com">박종범
 */
@Getter @Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class BsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    private String mbPhone; // Mobile Phone
    @NaturalId
    private String email;
    private String password;
    private boolean enabled;
    @CreationTimestamp
    private LocalDateTime addDate; // 유저 생성(=추가) 날짜
    private UserType userType;
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
    private boolean twoFAEnabled = false;
    private String twoFASecret;

    public String addedMonth() {
        int monInt = addDate.getMonthValue();
        int yearInt = addDate.getYear();
        return String.format("%d-%02d월", yearInt, monInt);
    }
}
