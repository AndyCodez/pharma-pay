package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.AuthRequest;
import com.andrew.pharmapay.payloads.AuthResponse;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final PharmacistRepository pharmacistRepository;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, PharmacistRepository pharmacistRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.pharmacistRepository = pharmacistRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse authenticateUser(AuthRequest request) throws Exception {
        logger.info("Authenticating user.");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            logger.warn("BadCredentialsException thrown");

            throw new Exception("Incorrect Username or Password", e);
        }

        Pharmacist pharmacist = (Pharmacist) this.pharmacistRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtUtil.generateToken(pharmacist);

        return new AuthResponse(jwtToken, pharmacist.getRole().name());
    }
}
