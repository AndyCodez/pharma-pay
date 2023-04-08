package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.RegistrationRequest;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import com.andrew.pharmapay.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PharmacistService {
    Logger logger = LoggerFactory.getLogger(PharmacistService.class);

    private final PharmacistRepository pharmacistRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public PharmacistService(PharmacistRepository pharmacistRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.pharmacistRepository = pharmacistRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public Pharmacist createPharmacist(RegistrationRequest request) {
        try {
            Pharmacist pharmacist =
                    new Pharmacist(
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            passwordEncoder.encode(request.getPassword()),
                            request.getRole());
            return pharmacistRepository.save(pharmacist);
        } catch (DataIntegrityViolationException e) {
            logger.error("DataIntegrityViolationException", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already taken");
        }
    }

    public List<Pharmacist> getAllPharmacists() {
        return pharmacistRepository.findAll();
    }
}
