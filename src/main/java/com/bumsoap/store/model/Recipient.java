package com.bumsoap.store.model;

import com.bumsoap.store.util.DoroZbun;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipient", // 실제 테이블명
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "full_name", "mb_phone", "doro_zbun",
            "address_detail", "addr_basis_id"
        }) // 실제 열 이름 목록
    })
@JsonIgnoreProperties({"bsOrders"})
public class Recipient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String fullName;
  private String mbPhone; // Mobile Phone

  @ManyToOne
  @JoinColumn(name="addr_basis_id", nullable = false)
  private AddressBasis addressBasis; // address basisID

  @Column(nullable = false)
  private DoroZbun doroZbun = DoroZbun.ROAD;
  private String addressDetail; // Detailed address

  @OneToMany(mappedBy = "recipient")
  private List<BsOrder> bsOrders = new ArrayList<>();

  public Recipient(String fullName,
                   String mbPhone,
                   AddressBasis addressBasis,
                   DoroZbun doroZbun,
                   String addressDetail) {
    this.fullName = fullName;
    this.mbPhone = mbPhone;
    this.addressBasis = addressBasis;
    this.doroZbun = doroZbun;
    this.addressDetail = addressDetail;
  }

  public void setDoroZbun(String dZlabel) {
    this.doroZbun = DoroZbun.valueOfLabel(dZlabel);
  }
}
