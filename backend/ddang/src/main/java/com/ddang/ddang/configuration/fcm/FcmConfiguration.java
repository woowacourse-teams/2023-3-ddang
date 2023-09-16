//package com.ddang.ddang.configuration.fcm;
//
//import com.ddang.ddang.configuration.fcm.exception.FcmNotFoundException;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import org.springframework.context.annotation.Profile;
//
//@Configuration
//@Profile("!test")
//public class FcmConfiguration {
//
//    @Value("${fcm.key.path}")
//    private String FCM_PRIVATE_KEY_PATH;
//
//    @Bean
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        final FileInputStream refreshToken = new FileInputStream(FCM_PRIVATE_KEY_PATH);
//        final List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
//
//        if (firebaseApps.isEmpty()) {
//            return makeNewInstance(refreshToken);
//        }
//
//        refreshToken.close();
//        return findExistingInstance(firebaseApps);
//    }
//
//    private FirebaseMessaging makeNewInstance(final InputStream refreshToken) throws IOException {
//        final FirebaseOptions options = FirebaseOptions.builder()
//                                                 .setCredentials(GoogleCredentials.fromStream(refreshToken))
//                                                 .build();
//        refreshToken.close();
//
//        return FirebaseMessaging.getInstance(FirebaseApp.initializeApp(options));
//    }
//
//    private FirebaseMessaging findExistingInstance(final List<FirebaseApp> firebaseApps) {
//        return firebaseApps.stream()
//                           .filter(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
//                           .findAny()
//                           .map(FirebaseMessaging::getInstance)
//                           .orElseThrow(FcmNotFoundException::new);
//    }
//}
