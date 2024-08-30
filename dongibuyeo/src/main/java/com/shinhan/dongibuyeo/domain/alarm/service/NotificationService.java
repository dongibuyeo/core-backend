package com.shinhan.dongibuyeo.domain.alarm.service;

import com.shinhan.dongibuyeo.domain.alarm.dto.NotificationRequest;
import com.shinhan.dongibuyeo.domain.challenge.entity.ChallengeType;
import com.shinhan.dongibuyeo.domain.challenge.entity.MemberChallenge;
import com.shinhan.dongibuyeo.domain.member.entity.Member;
import com.shinhan.dongibuyeo.domain.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final FCMService fcmService;

    public NotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    public void sendNotification(Member member,String title,String content) {

        if(member.getDeviceToken() == null || member.getDeviceToken().isEmpty()) {
            return;
        }

        NotificationRequest request = makeNotificationRequest(member, title, content);
        fcmService.send(request);
    }

    public void sendNotificationGroup(List<Member> members, String title, String content) {
        members.stream().forEach(member -> sendNotification(member, title, content));
    }

    public void sendNotificationMemberChallenges(List<MemberChallenge> memberChallenges, String title, String content) {
        memberChallenges.stream().map(x -> x.getMember()).forEach(member -> sendNotification(member, title, content));
    }

    private NotificationRequest makeNotificationRequest(Member member, String title, String body) {
        return new NotificationRequest(
                member.getDeviceToken(),
                title,
                body
        );
    }
}
