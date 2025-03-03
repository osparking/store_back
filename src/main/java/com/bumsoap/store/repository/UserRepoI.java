package com.bumsoap.store.repository;

import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.model.BsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepoI extends JpaRepository<BsUser, Long> {
    boolean existsByEmail(String email);

    @Query(nativeQuery = true,
           value = "select u.id, u.full_name, u.mb_phone, u.email, " +
                   "u.usable, w.dept, u.add_date, e.photo_id, p.image " +
                   "from bs_user u " +
                   "left join employee e on e.employee_id = u.id " +
                   "left join worker w on w.worker_id = e.employee_id " +
                   "left join photo p on p.id = e.photo_id " +
                   "where u.id = :id")
    Optional<UserDto> findUserDtoById(@Param("id")long id);
}
