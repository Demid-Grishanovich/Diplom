package com.datacrowd.backend.auth.controller;

import com.datacrowd.backend.auth.dto.AuthResponse;
import com.datacrowd.backend.auth.dto.LoginRequest;
import com.datacrowd.backend.auth.dto.RegisterRequest;
import com.datacrowd.backend.auth.service.AuthService;
import com.datacrowd.backend.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@AuthenticationPrincipal User currentUser) {
        AuthResponse response = authService.refresh(currentUser);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Здесь можно было бы добавить запись в blacklist, если бы мы его вели.
        return ResponseEntity.noContent().build();
    }
}
