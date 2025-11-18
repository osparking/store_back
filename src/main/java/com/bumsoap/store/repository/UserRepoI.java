package com.bumsoap.store.repository;

import com.bumsoap.store.dto.RecipientDto;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepoI extends JpaRepository<BsUser, Long> {

    boolean existsByEmail(String email);

    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE Bs_User u SET u.enabled = not u.enabled WHERE u.id = :userId")
    int toggleEnabledColumn(@Param("userId") Long userId);

    String selectUserDto =
            "select u.id, u.full_name, u.mb_phone, u.email, u.enabled," +
                    " w.dept, u.user_type, u.sign_up_method, u.two_fa_enabled," +
                    " u.add_date, e.photo_id, p.image " +
                    "from bs_user u" +
                    " left join employee e on e.employee_id = u.id" +
                    " left join worker w on w.worker_id = e.employee_id" +
                    " left join photo p on p.id = e.photo_id";
    String selectUserDtoById = selectUserDto + " where u.id = :id";

    @Query(nativeQuery = true,
            value = selectUserDtoById)
    Optional<UserDto> findUserDtoById(@Param("id") long id);

    @Query(nativeQuery = true, value = selectUserDto)
    List<UserDto> findAllUserDto();

    Optional<BsUser> findByEmail(String email);

    @Query(nativeQuery = true,
            value = "select bu.email from bs_user bu where email like " +
                    "'dummy%@email.com' order by email desc limit 1")
    Optional<String> findDummyEmailWithMaxNum();

    @Query(nativeQuery = true,
            value = """
                    SELECT r.full_name, r.mb_phone, ab.zipcode,
                    	r.doro_zbun, ab.road_address,
                    	ab.z_bun_address, r.address_detail
                    FROM bs_store.bs_user bu
                    join recipient r on r.id = bu.recipient
                    join address_basis ab on ab.id = r.addr_basis_id
                    where bu.id = :id;
                    """)
    Optional<RecipientDto> getRecipientById(@Param("id") long id);

    @Query(nativeQuery = true,
            value = """
                    SELECT r.full_name, r.mb_phone, ab.zipcode,
                    	r.doro_zbun, ab.road_address,
                    	ab.z_bun_address, r.address_detail
                    FROM bs_user bu
                    join bs_order bo on bo.user_id = bu.id
                    join recipient r on r.id = bo.recipient_id
                    join address_basis ab on ab.id = r.addr_basis_id
                    where bu.id = :id
                    order by bo.order_time desc
                    """)
    Page<RecipientDto> getPastRecipients(@Param("id") long id,
                                         Pageable pageable);
}
