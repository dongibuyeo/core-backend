package com.shinhan.dongibuyeo.domain.quiz.mapper;

import com.shinhan.dongibuyeo.domain.member.dto.response.MemberResponse;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.mapper.MemberMapper;
import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizMakeRequest;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizResponse;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizSolveResponse;
import com.shinhan.dongibuyeo.domain.quiz.entity.Quiz;
import com.shinhan.dongibuyeo.domain.quiz.entity.QuizMember;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class QuizMapper {

    private MemberMapper memberMapper;

    public QuizMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public Quiz toQuizEntity(QuizMakeRequest request) {
        return new Quiz(
                request.getQuestion(),
                request.getAnswer()
        );
    }

    public QuizResponse toQuizResponse(Quiz quiz) {
        return new QuizResponse(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getAnswer()
        );
    }

    public QuizSolveResponse toQuizSolveResponse(QuizMember quiz) {
        return new QuizSolveResponse(
                quiz.getId(),
                quiz.getSolvedAt()
        );
    }

    public List<MemberResponse> toWinnerResponse(HashMap<UUID, Member> members) {
        return members.values().stream().map(x -> memberMapper.toMemberResponse(x)).toList();
    }
}
