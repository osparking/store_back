package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
