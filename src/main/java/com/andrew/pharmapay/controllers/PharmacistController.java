package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.RegistrationRequest;
import com.andrew.pharmapay.services.PharmacistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class PharmacistController {

    final PharmacistService pharmacistService;

    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    // TODO: Only admin should have access to this endpoint in order to create pharmacists
    @PostMapping("/pharmacists")
    public ResponseEntity<Pharmacist> createPharmacistUser(@Valid @RequestBody RegistrationRequest request) {
        Pharmacist pharmacist= pharmacistService.createPharmacist(request);

        return ResponseEntity.ok(pharmacist);
    }

}
