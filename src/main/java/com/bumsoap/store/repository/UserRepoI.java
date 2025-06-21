package com.bumsoap.store.repository;

import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
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
            " w.dept, u.user_type, u.sign_up_method, u.add_date," +
            " e.photo_id, p.image " +
            "from bs_user u" +
            " left join employee e on e.employee_id = u.id" +
            " left join worker w on w.worker_id = e.employee_id" +
            " left join photo p on p.id = e.photo_id";
    String selectUserDtoById = selectUserDto + " where u.id = :id";

    @Query(nativeQuery = true,
           value = selectUserDtoById)
    Optional<UserDto> findUserDtoById(@Param("id")long id);

    @Query(nativeQuery = true, value = selectUserDto)
    List<UserDto> findAllUserDto();

    Optional<BsUser> findByEmail(String email);

    @Query(nativeQuery = true,
            value="select bu.email from bs_user bu where email like " +
                    "'dummy%@email.com' order by email desc limit 1")
    Optional<String> findDummyEmailWithMaxNum();
}
