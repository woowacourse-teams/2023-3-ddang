package com.ddang.ddang.configuration.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FcmConfiguration {

    private static final FirebaseOptions TEST_OPTIONS = FirebaseOptions.builder()
                                                                       .setCredentials(new MockGoogleCredentials("test-token"))
                                                                       .setProjectId("test-project")
                                                                       .build();
    private static final FirebaseApp FIREBASE_APP = FirebaseApp.initializeApp(TEST_OPTIONS);

    @Value("${fcm.enabled}")
    private boolean enabled;

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        if (!enabled) {
            return FirebaseMessaging.getInstance(FIREBASE_APP);
        }

        final ClassPathResource resource = new ClassPathResource(FCM_PRIVATE_KEY_PATH);
        final InputStream refreshToken = resource.getInputStream();

        final List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (firebaseApps.isEmpty()) {
            return makeNewInstance(refreshToken);
        }

        return findExistingInstance(firebaseApps);
    }

    private FirebaseMessaging makeNewInstance(final InputStream refreshToken) throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                                                 .setCredentials(GoogleCredentials.fromStream(refreshToken))
                                                 .build();
        return FirebaseMessaging.getInstance(FirebaseApp.initializeApp(options));
    }

    private FirebaseMessaging findExistingInstance(final List<FirebaseApp> firebaseApps) {
        return firebaseApps.stream()
                           .filter(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                           .findAny()
                           .map(FirebaseMessaging::getInstance)
                           .orElse(null);
    }
}
