package com.shinhan.dongibuyeo.domain.quiz.service;

import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizMakeRequest;
import com.shinhan.dongibuyeo.domain.quiz.dto.request.QuizSolveRequest;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizResponse;
import com.shinhan.dongibuyeo.domain.quiz.dto.response.QuizSolveResponse;
import com.shinhan.dongibuyeo.domain.quiz.entity.Quiz;
import com.shinhan.dongibuyeo.domain.quiz.entity.QuizMember;
import com.shinhan.dongibuyeo.domain.quiz.mapper.QuizMapper;
import com.shinhan.dongibuyeo.domain.quiz.repository.QuizMemberRepository;
import com.shinhan.dongibuyeo.domain.quiz.repository.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {
    private final QuizMemberRepository quizMemberRepository;
    private QuizRepository quizRepository;
    private MemberService memberService;
    private QuizMapper quizMapper;

    public QuizService(QuizRepository quizRepository, MemberService memberService, QuizMapper quizMapper, QuizMemberRepository quizMemberRepository) {
        this.quizRepository = quizRepository;
        this.memberService = memberService;
        this.quizMapper = quizMapper;
        this.quizMemberRepository = quizMemberRepository;
    }

    @Transactional
    public QuizResponse makeQuiz(QuizMakeRequest request) {
        Quiz quiz = quizRepository.save(quizMapper.toQuizEntity(request));
        return quizMapper.toQuizResponse(quiz);
    }

    @Transactional
    public QuizResponse getRandomQuiz(UUID memberId) {
        Member member = memberService.getMemberById(memberId);

        Quiz quiz = quizRepository.findAll(getRandomIndex()).stream().findFirst().orElseThrow(
                EntityNotFoundException::new
        );

        return quizMapper.toQuizResponse(quiz);
    }

    @Transactional
    public Pageable getRandomIndex() {
        long count = quizRepository.count();
        int idx =  new Random().nextInt((int) count);
        return PageRequest.of(idx,1);
    }

    @Transactional
    public QuizSolveResponse solveQuiz(QuizSolveRequest request) {
        Quiz quiz = getQuizById(request.getQuizId());
        Member member = memberService.getMemberById(request.getMemberId());

        QuizMember quizMember = new QuizMember(member, quiz);
        member.getQuizMembers().add(quizMember);

        return quizMapper.toQuizSolveResponse(quizMember);
    }

    private Quiz getQuizById(UUID quizId) {
        return quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Boolean alreadyToday(UUID memberId) {
        LocalDateTime now = LocalDateTime.now();
        return quizMemberRepository.existsByMemberAndDate(memberId,now.getYear(),now.getMonthValue(),now.getDayOfMonth());
    }


    @Transactional
    public List<QuizSolveResponse> getMemberDateSolvedList(UUID memberId, Integer year, Integer month) {
        List<QuizMember> solvedList = quizMemberRepository.findAllByMemberAndDate(year,month,memberId);
        return solvedList.stream().map(x -> quizMapper.toQuizSolveResponse(x)).toList();
    }

    @Transactional
    public List getWinnerOfMonth(int year, int month) {
        List<QuizMember> solvedList = quizMemberRepository.findWinnerByYearAndMonth(year,month);
        Collections.shuffle(solvedList);

        HashMap<UUID, Member> winners = new HashMap<>();

        for(QuizMember quizMember : solvedList) {
            winners.put(quizMember.getMember().getId(), quizMember.getMember());

            if(winners.size() >= 42)
                break;
        }

        return quizMapper.toWinnerResponse(winners);
    }


}
