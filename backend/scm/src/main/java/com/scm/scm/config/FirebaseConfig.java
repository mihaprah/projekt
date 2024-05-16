package com.scm.scm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        String firebaseConfig = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");
        if (firebaseConfig == null) {
            throw new RuntimeException("Firebase service account key not found in environment variables");
        }

        Map<String, Object> firebaseCredentials = new Gson().fromJson(firebaseConfig, Map.class);
        InputStream serviceAccount = new ByteArrayInputStream(new Gson().toJson(firebaseCredentials).getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}