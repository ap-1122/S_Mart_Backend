 package com.example.demo.service;

import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final String googleClientId;

    // Manual Constructor (Lombok issue hatane ke liye)
    @Autowired
    public GoogleAuthService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            @Value("${google.client-id}") String googleClientId
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.googleClientId = googleClientId;
    }

    public String authenticateGoogleUser(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
            .setAudience(Collections.singletonList(googleClientId))
            .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google Token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Database Logic
            UserModel user = userRepository.findByEmail(email).orElseGet(() -> {
                UserModel newUser = new UserModel();
                // Ab ye line error nahi degi kyunki UserModel me humne method bana diya hai
                newUser.setEmail(email);
                newUser.setUsername(name);
                newUser.setPassword(null);
                newUser.setAuthProvider("GOOGLE");
                return userRepository.save(newUser);
            });

//            return jwtUtil.generateToken(user.getEmail());
         // GoogleAuthService.java ke andar
            return jwtUtil.generateToken(user.getEmail(), user.getRole()); // role add kar dena

        } catch (Exception e) {
            throw new RuntimeException("Google Authentication Failed: " + e.getMessage());
        }
    }
}