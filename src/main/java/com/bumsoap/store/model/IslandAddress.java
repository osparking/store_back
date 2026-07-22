package com.bumsoap.store.model;

import com.bumsoap.store.util.YesNoConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "island_address")           // 실제 테이블명
@Check(name = "is_jeju", constraints = "is_jeju IN ('Y', 'N')")
@Check(name = "is_island", constraints = "is_island IN ('Y', 'N')")
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
    @Column(name = "is_jeju", columnDefinition = "CHAR(1)")
    private Boolean isJeju;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "is_island", columnDefinition = "CHAR(1)")
    private Boolean isIsland;
}