package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.FailedResponse;
import com.andrew.pharmapay.services.PharmacistService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PharmacistController {

    final PharmacistService pharmacistService;

    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    @PostMapping("/pharmacists")
    public ResponseEntity<?> createPharmacistUser(@Valid @RequestBody Pharmacist pharmacist) {
        Pharmacist createdPharmacist= pharmacistService.createPharmacist(pharmacist);

        return ResponseEntity.created(
                URI.create("/api/v1/pharmacists/" + createdPharmacist.getId())
        ).body(createdPharmacist);
    }

}
