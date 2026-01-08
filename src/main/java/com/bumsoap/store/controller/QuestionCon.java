package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.QuestionDto;
import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.QuestionSaveReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.question.QuestionServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bumsoap.store.dto.ReviewRow.formatKoreanDateTime;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.QUESTION)
@RequiredArgsConstructor
public class QuestionCon {
    private final ObjMapper objMapper;
    private final QuestionServI questionServ;
    private final UserRepoI userRepoI;

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

    private void emailAdmin(Long userId, QuestionDto mappedOne) {
        System.out.println("고객 이메일: " + userRepoI.getEmailById(userId));
        System.out.println("등록 시각: "
                + formatKoreanDateTime(mappedOne.getInsertTime()));
        System.out.println("질문 제목: " + mappedOne.getTitle());
    }

    }

}
