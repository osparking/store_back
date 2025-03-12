package com.bumsoap.store.model;

import com.bumsoap.store.util.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

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
    private String email;
    private String password;
    private boolean usable;
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
}
