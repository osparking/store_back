package com.bumsoap.store.repository;

import com.bumsoap.store.dto.RecipientDto;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.util.UserType;
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
                    SELECT mr.full_name, mr.mb_phone, mr.zipcode,
                        mr.doro_zbun, mr.road_address,
                        mr.z_bun_address,mr.address_detail
                    FROM my_recipients mr
                    WHERE mr.user_id = :id;
                    """)
    Page<RecipientDto> getPastRecipients(@Param("id") long id,
                                         Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select 	sum(oi.count ) soaps,
                    	DATE_FORMAT(bo.order_time , '%Y-%m') month
                    from bs_order bo, order_item oi
                    where bo.user_id = :id and
                    	(bo.order_status = 8 or
                    		bo.order_status = 9) and
                    	oi.order_id = bo.id and
                    	bo.order_time >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH)
                    group by month;
                    """)
    List<Object[]> soapsMonthOfUser(@Param("id") long id);

    @Query("SELECT b.email FROM BsUser b WHERE b.userType = :userType")
    Optional<String> findEmailByUserType(@Param("userType") UserType userType);
}
