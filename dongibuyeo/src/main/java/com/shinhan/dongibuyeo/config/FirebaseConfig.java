package com.shinhan.dongibuyeo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String FIREBASE_KEY_PATH = "dongibuyeo-39d99-firebase-adminsdk-n2ef2-274029e929.json";

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())).build();

            FirebaseApp.initializeApp(options);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
