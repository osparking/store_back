package com.bumsoap.store.model;

import com.bumsoap.store.util.DoroZbun;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
}
