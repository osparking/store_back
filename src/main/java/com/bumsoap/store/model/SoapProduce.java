package com.bumsoap.store.model;

import com.bumsoap.store.util.BsShape;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SoapProduce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BsShape bsShape;
    private Long quantity;
    private LocalDate produceDate;
    private Long producerId; // 생산직원 ID
    private Long registerId; // 등록직원 ID

    @Column(name = "add_time", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registerTime = LocalDateTime.now(); // 등록 시간
}
