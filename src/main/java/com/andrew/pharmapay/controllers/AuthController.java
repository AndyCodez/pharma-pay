package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.payloads.AuthRequest;
import com.andrew.pharmapay.payloads.AuthResponse;
import com.andrew.pharmapay.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        return ResponseEntity.ok(authService.authenticateUser(authRequest));
    }
}
