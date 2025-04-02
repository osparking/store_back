package com.bumsoap.store.service.user;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.exception.DataNotFoundException;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServ implements UserServInt {
    private final UserRepoI userRepo;
    private final ObjMapper mapper;

    @Override
    public int toggleEnabledColumn(Long id) {
        return userRepo.toggleEnabledColumn(id);
    }

    @Override
    public List<UserDto> getUserDtoList() {
        return userRepo.findAllUserDto();
    }

    @Override
    public void deleteById(Long id) {
        userRepo.findById(id).ifPresentOrElse(
                userRepo::delete,
                () -> new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id)
        );
    }

    @Override
    public BsUser findById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id));
    }

    @Override
    public BsUser getUserById(Long id) {
        Optional<BsUser> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        Optional<UserDto> userOptional = userRepo.findUserDtoById(id);

        if (userOptional.isPresent()) {
            UserDto userDto = userOptional.get();
            return userDto;
        } else {
            throw new IdNotFoundEx(Feedback.USER_ID_NOT_FOUND + id);
        }
    }

    @Override
    public BsUser getByEmail(String email) {
        Optional<BsUser> user = userRepo.findByEmail(email);
        return user.orElseThrow(() -> new DataNotFoundException(
                Feedback.NOT_FOUND_EMAIL + email));
    }

    @Override
    public Map<String, Map<String, Long>> countUsersByMonthAndType() {
        List<BsUser> users = userRepo.findAll();
        var mapMonthType = users.stream().collect(Collectors.groupingBy(
                BsUser::addedMonth,
                Collectors.groupingBy(user -> user.getUserType().label,
                        Collectors.counting())));
        var sorted = new TreeMap<String, Map<String, Long>>();
        sorted.putAll(mapMonthType);
        return sorted;
    }

    @Override
    public long countAll() {
        return userRepo.count();
    }
}
