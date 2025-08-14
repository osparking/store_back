package com.bumsoap.store.repository;

import com.bumsoap.store.model.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecipientRepoI extends JpaRepository<Recipient, Long> {
  @Query(nativeQuery = true, value =
      "select r.* from recipient r where " +
          "r.addr_basis_id = :addr_basis_id " +
          "and r.address_detail = :address_detail " +
          "and r.doro_zbun = :doro_zbun " +
          "and r.full_name = :full_name " +
          "and r.mb_phone = :mb_phone " +
          "limit 1")
  Optional<Recipient> getIdenticalRecipient(
      @Param("addr_basis_id") Long addr_basis_id,
      @Param("address_detail") String address_detail,
      @Param("doro_zbun") int doro_zbun,
      @Param("full_name") String full_name,
      @Param("mb_phone") String mb_phone);
}
