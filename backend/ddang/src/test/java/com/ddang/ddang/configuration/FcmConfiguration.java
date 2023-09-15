package com.ddang.ddang.configuration;

import com.ddang.ddang.configuration.fcm.MockGoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class FcmConfiguration {

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        final FirebaseApp firebaseApps = findFirebaseApps();

        return FirebaseMessaging.getInstance(firebaseApps);
    }

    private FirebaseApp findFirebaseApps() {
        final List<FirebaseApp> apps = FirebaseApp.getApps();

        if (!apps.isEmpty()) {
            for (final FirebaseApp app : apps) {
                if (FirebaseApp.DEFAULT_APP_NAME.equals(app.getName())) {
                    return app;
                }
            }
        }

        final FirebaseOptions options = FirebaseOptions.builder()
                                                       .setCredentials(new MockGoogleCredentials("test-token"))
                                                       .setProjectId("test-project")
                                                       .build();

        return FirebaseApp.initializeApp(options);
    }
}
