package com.bumsoap.store.repository;

import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepoI extends JpaRepository<BsUser, Long> {

    boolean existsByEmail(String email);

    String selectUserDto =
            "select u.id, u.full_name, u.mb_phone, u.email, u.usable," +
            " w.dept, u.user_type, u.add_date, e.photo_id, p.image " +
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
}
