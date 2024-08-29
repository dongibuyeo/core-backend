package com.shinhan.dongibuyeo.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TopRankerInfo {
    private String nickname;
    private String email;
    private String profileImage;
    private int score;
}
