package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.QuestionDto;
import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.UnauthorizedException;
import com.bumsoap.store.exception.UserTypeNotFouncEx;
import com.bumsoap.store.question.QuestionRow;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.QuestionSaveReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.question.QuestionServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import com.bumsoap.store.util.UserType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final UserRepoI userRepoI;
    private final EmailManager emailManager;

    /**
     * 현 로그인 유저가 읽으려는 질문에 유자격 자이면 질문 자료를 반응함.
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
        if (htmlContent == null) {
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
