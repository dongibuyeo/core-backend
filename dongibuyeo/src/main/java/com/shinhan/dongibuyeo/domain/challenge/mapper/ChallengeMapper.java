package com.shinhan.dongibuyeo.domain.challenge.mapper;

import com.shinhan.dongibuyeo.domain.challenge.dto.request.ChallengeRequest;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.dto.response.MemberChallengeDetail;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChallengeMapper {

    @Mapping(source = "challenge.id", target = "challengeId")
    @Mapping(source = "challenge.account.accountNo", target = "accountNo")
    @Mapping(source = "challenge.startDate", target = "startDate", dateFormat = "yyyyMMdd")
    @Mapping(source = "challenge.endDate", target = "endDate", dateFormat = "yyyyMMdd")
    ChallengeResponse toChallengeResponse(Challenge challenge);

    @Mapping(source = "startDate", target = "startDate", qualifiedByName = "stringToLocalDate")
    @Mapping(source = "endDate", target = "endDate", qualifiedByName = "stringToLocalDate")
    Challenge toChallengeEntity(ChallengeRequest challengeRequest);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(date, formatter);
    }

    @Mapping(source = "memberChallenge.challenge.id", target = "challengeId")
    @Mapping(source = "memberChallenge.challenge.type", target = "type")
    @Mapping(source = "memberChallenge.challenge.status", target = "status")
    @Mapping(source = "memberChallenge.challenge.account.accountNo", target = "accountNo")
    @Mapping(source = "memberChallenge.challenge.startDate", target = "startDate", dateFormat = "yyMMdd")
    @Mapping(source = "memberChallenge.challenge.endDate", target = "endDate", dateFormat = "yyMMdd")
    @Mapping(source = "memberChallenge.challenge.title", target = "title")
    @Mapping(source = "memberChallenge.challenge.description", target = "description")
    @Mapping(source = "memberChallenge.challenge.image", target = "image")
    @Mapping(source = "memberChallenge.challenge.totalDeposit", target = "totalDeposit")
    @Mapping(source = "memberChallenge.challenge.participants", target = "participants")
    @Mapping(source = "memberChallenge.status", target = "memberStatus")
    @Mapping(source = "memberChallenge.deposit", target = "memberDeposit")
    MemberChallengeDetail toMemberChallengeResponse(MemberChallenge memberChallenge);
}
