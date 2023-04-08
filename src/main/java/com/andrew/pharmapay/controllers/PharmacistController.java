package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.RegistrationRequest;
import com.andrew.pharmapay.services.PharmacistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class PharmacistController {

    final PharmacistService pharmacistService;

    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    @PostMapping("/pharmacists")
    public ResponseEntity<Pharmacist> createPharmacistUser(@Valid @RequestBody RegistrationRequest request) {
        Pharmacist pharmacist= pharmacistService.createPharmacist(request);

        return ResponseEntity.created(URI.create("/api/v1/pharmacists" + pharmacist.getId())).body(pharmacist);
    }

}
