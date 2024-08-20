package com.shinhan.dongibuyeo.domain.member.mapper;

import com.shinhan.dongibuyeo.domain.member.dto.request.MemberSaveRequest;
import com.shinhan.dongibuyeo.domain.member.dto.response.MemberLoginResponse;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

    Member toMemberEntity(MemberSaveRequest request);

    MemberLoginResponse toMemberLoginResponse(Member member);
}
