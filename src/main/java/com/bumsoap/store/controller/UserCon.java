package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.RecipientDto;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.dto.UserDto;
import com.bumsoap.store.event.UserRegisterEvent;
import com.bumsoap.store.exception.ExistingEmailEx;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.model.*;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.PasswordChangeReq;
import com.bumsoap.store.request.UserRegisterReq;
import com.bumsoap.store.request.UserUpdateReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.security.user.BsUserDetailsService;
import com.bumsoap.store.service.AdminServ;
import com.bumsoap.store.service.CustomerServ;
import com.bumsoap.store.service.password.PasswordChangeServInt;
import com.bumsoap.store.service.role.RoleServInt;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.service.worker.WorkerServInt;
import com.bumsoap.store.util.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMap.USER)
@RequiredArgsConstructor
public class UserCon {
    private final ObjMapper objMapper;
    private final AdminServ adminServ;
    private final CustomerServ customerServ;
    private final WorkerServInt workerServ;
    private final UserRepoI userRepo;
    private final UserServInt userServ;
    private final PasswordChangeServInt passwordChangeServ;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final RoleServInt roleServ;
    private final BsUserDetailsService bsUserDetailsService;
    private final BsParameters provider;

    @GetMapping(UrlMap.GET_MAX_SUFFIX)
    public ResponseEntity<ApiResp> getMaxDummyEmailSuffix() {
        try {
            String email = userServ.findDummyEmailWithMaxNum();
            String suffix = null;
            if (email!=null) {
                suffix = email.substring(5, 9);
            }
            return ResponseEntity.ok(new ApiResp(email==null ?
                    Feedback.NOT_FOUND_EMAIL + "dummy email":Feedback.FOUND,
                    suffix));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMap.CHANGE_PASSWORD)
    public ResponseEntity<ApiResp> changePassword(
            @PathVariable("id") Long id, @RequestBody PasswordChangeReq request) {
        try {
            passwordChangeServ.changePwd(id, request);
            return ResponseEntity.ok(new ApiResp(Feedback.PASSWORD_CHANGED, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResp(e.getMessage(), null));
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_USER_DTO_BY_ID)
    public ResponseEntity<ApiResp> getUserDtoById(@PathVariable("id") Long id) {
        try {
            if (BsUtils.isQualified(id, false, null)) {
                UserDto userDto = userServ.getUserDtoById(id);
                return ResponseEntity.ok(new ApiResp(Feedback.USER_DTO_BY_ID, userDto));
            } else {
                return ResponseEntity.status(UNAUTHORIZED).body(
                        new ApiResp(Feedback.NOT_QUALIFIED_FOR + id, null));
            }
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_RECIPIENT)
    public ResponseEntity<ApiResp> getRecipientById(@PathVariable("id") Long id) {
        try {
            RecipientDto recipient = userServ.getRecipientById(id);

            return ResponseEntity.ok(new ApiResp(recipient==null ?
                    Feedback.NO_DEFAULT_RECIPIENT:
                    Feedback.DEFAULT_RECIPIENT,
                    recipient));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_RECIPIENTS)
    public ResponseEntity<ApiResp> getRecipientsById(
            @Valid @RequestParam("id") Long id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(provider.getPageSize());
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
            Page<RecipientDto> myRecipientPage = userRepo.getPastRecipients(id, pageable);
            int totalPages = myRecipientPage.getTotalPages();
            List<Integer> pageNumbers = null;

            if (totalPages > 0) {
                pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
            }

            var result = new SearchResult<RecipientDto>(myRecipientPage,
                    myRecipientPage.getNumber() + 1,
                    pageSize,
                    totalPages,
                    pageNumbers
            );
            return ResponseEntity.ok(new ApiResp(Feedback.MY_RECIPIENTS_FOUND,
                    result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_ALL)
    public ResponseEntity<ApiResp> getAllUser() {
        try {
            return ResponseEntity.ok().body(
                    new ApiResp("유저 전체 목록", userServ.getUserDtoList()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @DeleteMapping(UrlMap.DELETE_BY_ID)
    public ResponseEntity<ApiResp> delete(@PathVariable("id") Long id) {
        try {
            if (BsUtils.isQualified(id, false, null)) {
                String deletedName = userServ.deleteById(id);
                return ResponseEntity.ok(
                        new ApiResp(Feedback.DELETEED_USER_NAME + deletedName, null));
            } else {
                return ResponseEntity.status(UNAUTHORIZED).body(
                        new ApiResp(Feedback.NOT_QUALIFIED_FOR + id, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_BY_ID)
    public ResponseEntity<ApiResp> getUser(@PathVariable("id") Long id) {
        try {
            BsUser user = userServ.getUserById(id);
            if (user==null) {
                String msg = "존재하지 않는 아이디: " + id;
                return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND)
                        .body(new ApiResp(msg, null));
            } else {
                return ResponseEntity.ok(new ApiResp("유저 발견됨", user));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.GET_DETAILS)
    public ResponseEntity<ApiResp> getUserDetails(@AuthenticationPrincipal
                                                  UserDetails userDetails) {
        try {
            var details = bsUserDetailsService.loadUserByUsername(
                    userDetails.getUsername());
            return ResponseEntity.ok(new ApiResp("유저 발견됨", details));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @PutMapping(UrlMap.UPDATE)
    public ResponseEntity<ApiResp> update(@PathVariable("id") Long id,
                                          @RequestBody UserUpdateReq request) {
        try {
            BsUser user = userServ.getUserById(id);

            if (!BsUtils.isQualified(id, true, user.getUserType())) {
                return ResponseEntity.status(UNAUTHORIZED).body(
                        new ApiResp(Feedback.NOT_QUALIFIED_FOR + id, null));
            }
            user.setFullName(request.getFullName());
            user.setMbPhone(request.getMbPhone());
            user.setEnabled(request.isEnabled());

            switch (UserType.valueOfLabel(request.getUserType())) {
                case CUSTOMER:
                    var customer = objMapper.mapToDto(user, Customer.class);
                    user = customerServ.add(customer);
                    break;

                case WORKER:
                    var worker = objMapper.mapToDto(user, Worker.class);
                    worker.setDept(request.getDept());
                    user = workerServ.add(worker);
                    break;

                case ADMIN:
                    var admin = objMapper.mapToDto(user, Admin.class);
                    user = adminServ.add(admin);
                    break;

                case null, default:
                    throw new IllegalArgumentException(Feedback.USER_TYPE_WRONG);
            }
            var userDto = objMapper.mapToDto(user, UserDto.class);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.USER_UPDATE_SUCCESS, userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResp(e.getMessage(), null));
        }
    }

    private final VerifinTokenServInt tokenService;

    @PostMapping(UrlMap.ADD)
    public ResponseEntity<ApiResp> add(@RequestBody UserRegisterReq request) {
        String encodedPwd = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPwd);
        String email = request.getEmail();
        BsUser user = null;
        request.setSignUpMethod("EMAIL");
        try {
            if (userRepo.existsByEmail(email)) {
                throw new ExistingEmailEx(Feedback.USER_TAKEN_EMAIL + email);
            }
            switch (request.getUserType()) {
                case ADMIN:
                    var admin = objMapper.mapToDto(request, Admin.class);
                    Role adminRole = roleServ.findByName("ROLE_ADMIN");
                    admin.setRoles(Set.of(adminRole));
                    user = adminServ.add(admin);
                    break;
                case CUSTOMER:
                    var customer = objMapper.mapToDto(request, Customer.class);
                    Role customerRole = roleServ.findByName("ROLE_CUSTOMER");
                    customer.setRoles(Set.of(customerRole));
                    user = customerServ.add(customer);
                    break;
                case WORKER:
                    var worker = objMapper.mapToDto(request, Worker.class);
                    Role workerRole = roleServ.findByName("ROLE_WORKER");
                    worker.setRoles(Set.of(workerRole));
                    user = workerServ.add(worker);
                    break;
                default:
                    throw new IllegalArgumentException(Feedback.USER_TYPE_WRONG);
            }
            publisher.publishEvent(new UserRegisterEvent(user));

            var userDto = objMapper.mapToDto(user, UserDto.class);
            String verifToken = UUID.randomUUID().toString();

            tokenService.saveTokenForUser(verifToken, user);
            userDto.setVerifToken(verifToken);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.USER_ADD_SUCCESS, userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResp(e.getMessage(), null));
        }

    }
}
