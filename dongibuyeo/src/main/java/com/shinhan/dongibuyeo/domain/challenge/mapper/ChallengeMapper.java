package com.shinhan.dongibuyeo.domain.challenge.mapper;

import com.shinhan.dongibuyeo.domain.challenge.dto.response.ChallengeResponse;
import com.shinhan.dongibuyeo.domain.challenge.entity.Challenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChallengeMapper {

    @Mapping(source = "challenge.id", target = "challengeId")
    @Mapping(source = "challenge.account.accountNo", target = "accountNo")
    @Mapping(target = "deposit", expression = "java(challenge.getDeposit().get())")
    @Mapping(target = "participants", expression = "java(challenge.getParticipants().get())")
    ChallengeResponse toChallengeResponse(Challenge challenge);
}
