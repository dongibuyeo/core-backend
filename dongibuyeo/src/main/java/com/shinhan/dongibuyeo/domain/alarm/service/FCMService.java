package com.shinhan.dongibuyeo.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.shinhan.dongibuyeo.domain.alarm.dto.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public void send(final NotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.getToken())
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl","300")
                        .setNotification(new WebpushNotification(request.getTitle(), request.getContent()))
                        .build()).build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
