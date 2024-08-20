package com.shinhan.dongibuyeo.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    private String profileImage;

    @NotBlank
    private String deviceToken;
}