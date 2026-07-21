package com.bumsoap.store.model;

import com.bumsoap.store.util.YesNoConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "island_address")           // 실제 테이블명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IslandAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5, nullable = false, unique = true)
    private String zipcode;

    @Column(nullable = false, length = 255)
    private String address;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "is_jeju", columnDefinition = "CHAR(1) DEFAULT 'N'")
    private Boolean isJeju;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "is_island", columnDefinition = "CHAR(1) DEFAULT 'N'")
    private Boolean isIsland;
}