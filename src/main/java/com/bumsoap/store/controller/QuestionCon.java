package com.bumsoap.store.controller;

import com.bumsoap.store.dto.FollowUpDto;
import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.QuestionDto;
import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.UnauthorizedException;
import com.bumsoap.store.exception.UserTypeNotFouncEx;
import com.bumsoap.store.model.Question;
import com.bumsoap.store.question.QuestionRow;
import com.bumsoap.store.repository.QuestionRepo;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.FollowUpData;
import com.bumsoap.store.request.QuestionSaveReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.service.question.QuestionServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import com.bumsoap.store.util.UserType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

import static com.bumsoap.store.dto.ReviewRow.formatKoreanDateTime;
import static com.bumsoap.store.util.BsUtils.isQualified;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.QUESTION)
@RequiredArgsConstructor
public class QuestionCon {
    private final ObjMapper objMapper;
    private final QuestionServI questionServ;
    private final QuestionRepo questionRepo;
    private final UserRepoI userRepoI;
    private final EmailManager emailManager;

    @PostMapping(value = UrlMap.FOLLOW_UP)
    public ResponseEntity<ApiResp> addFollowUp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FollowUpData answerData) {
        try {
            var user = (BsUserDetails) userDetails;
            answerData.setWriterId(user.getId());

            var savedOne = questionServ.handleSaveFollowUp(answerData);
            var mappedOne = objMapper.mapToDto(savedOne, FollowUpDto.class);

            /**
             * 저자가 관리자면, 질문자에게 이메일 통지
             */
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                sayAnswered(answerData.getQuestionId(), mappedOne);
            }
            return ResponseEntity.ok(new ApiResp(Feedback.FOLLOW_UP_SAVED,
                    mappedOne));
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    /**
     * 현 로그인 유저가 읽으려는 질문에 유자격 자이면 질문 자료를 반응함.
     *
     * @param id 질문 ID
     * @return 질문 상세 정보
     */
    @GetMapping(UrlMap.GET_QUESTION)
    public ResponseEntity<ApiResp> getQuestionInfo(@PathVariable("id") Long id) {
        try {
            QuestionRow question = questionServ.findById(id);
            if (isQualified(question.getUserId(), false, null)) {
                return ResponseEntity.ok(new ApiResp("질문 읽어옴", question));
            } else {
                throw new UnauthorizedException(Feedback.NOT_MY_QUESTION);
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.MY_QUESTIONS)
    public ResponseEntity<ApiResp> getMyQuestionsPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        try {
            var user = (BsUserDetails) userDetails;
            Pageable pageable = PageRequest.of(page - 1, size);
            var questionPage = questionServ.getQuestionsPage(
                    page, size, user.getId());

            return ResponseEntity.ok(
                    new ApiResp("내 질문 페이지", questionPage));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @PostMapping(UrlMap.ADD)
    public ResponseEntity<ApiResp> addQuestion(
            @RequestBody QuestionSaveReq addOrderReq) {
        try {
            var savedOne = questionServ.handleSaveQuestion(addOrderReq);
            var mappedOne = objMapper.mapToDto(savedOne, QuestionDto.class);

            emailAdmin(addOrderReq.getUserId(), mappedOne);
            return ResponseEntity.ok(new ApiResp(Feedback.QUESTION_SAVED,
                    mappedOne));
        } catch (IdNotFoundEx e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResp(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    private void sayAnswered(Long questionId, FollowUpDto mappedOne)
            throws MessagingException, UnsupportedEncodingException {
        Question question = questionRepo.findById(questionId).orElseThrow(
                () -> new IdNotFoundEx("부재 질문 ID: " + questionId));
        var receiverEmail = question.getUser().getEmail();

        String subject = "답변 등록 안내";
        String senderName = "범이비누";
        String EMAIL_TEMPLATE = """
                <p>범이비누 고객님, 안녕하세요?</p>
                <p>귀하께서 등록한 질문에 대한 답변이 등록되었습니다. 답변 요약:</p>
                <ul>
                    <li>질문 제목: %s</li>
                    <li>답변 시각: %s</li>
                    <li>답변 서두(100자): %s</li>
                </ul>
                <br>- 범이비누 질문 등록 알림 체계""";

        String content = String.format(EMAIL_TEMPLATE,
                mappedOne.getQuestionTitle(),
                formatKoreanDateTime(mappedOne.getInsertTime()),
                getPlainContent(mappedOne.getFollowUpContent(), 100));

        emailManager.sendMail(receiverEmail,
                subject,
                senderName,
                content);
    }

    private void emailAdmin(Long userId, QuestionDto mappedOne)
            throws MessagingException, UnsupportedEncodingException {

        var adminEmail = userRepoI.findEmailByUserType(UserType.ADMIN)
                .orElseThrow(() -> new UserTypeNotFouncEx(
                        "부재 유저 유형: " + UserType.ADMIN.toString()));
        String subject = "고객 질문 접수 안내";
        String senderName = "범이비누";
        String EMAIL_TEMPLATE = """
                <p>안녕하세요? 범이비누 Q&A 담당 직원님</p>
                <p>범이비누 고객님이 질문을 올리셔서 알려드립니다. 질문 요약:</p>
                <ul>
                    <li>질문 제목: %s</li>
                    <li>등록 시각: %s</li>
                    <li>고객 메일: %s</li>
                    <li>질문 서두: %s</li>
                </ul>
                <br>- 범이비누 질문 등록 알림 체계""";

        String content = String.format(EMAIL_TEMPLATE,
                mappedOne.getTitle(),
                formatKoreanDateTime(mappedOne.getInsertTime()),
                userRepoI.getEmailById(userId),
                getPlainContent(mappedOne.getQuestion(), 100));

        emailManager.sendMail(adminEmail,
                subject,
                senderName,
                content);
    }

    public static String getPlainContent(String htmlContent, int maxLength) {
        if (htmlContent==null) {
            return "";
        }

        String plainContent = stripHtmlTags(htmlContent);
        return truncateWithEllipsis(plainContent, maxLength);
    }

    private static String stripHtmlTags(String html) {
        return html.replaceAll("<[^>]*>", "").trim();
    }

    private static String truncateWithEllipsis(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
