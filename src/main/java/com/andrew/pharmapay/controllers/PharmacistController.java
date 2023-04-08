package com.andrew.pharmapay.controllers;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.payloads.PharmacistResponse;
import com.andrew.pharmapay.payloads.RegistrationRequest;
import com.andrew.pharmapay.services.PharmacistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

        return ResponseEntity.created(URI.create("/api/v1/pharmacists/" + pharmacist.getId())).body(pharmacist);
    }

    @GetMapping("/pharmacists")
    public ResponseEntity<List<PharmacistResponse>> listPharmacists() {
        List<Pharmacist> pharmacistList = pharmacistService.getAllPharmacists();
        List<PharmacistResponse> pharmacistResponses = new ArrayList<>();

        for (Pharmacist pharmacist : pharmacistList){
            PharmacistResponse pharmacistResponse = new PharmacistResponse(
                    pharmacist.getFirstName(),
                    pharmacist.getLastName(),
                    pharmacist.getEmail(),
                    pharmacist.getRole(),
                    pharmacist.getCreatedBy(),
                    pharmacist.getCreatedDate(),
                    pharmacist.getLastModifiedBy(),
                    pharmacist.getLastModifiedDate()
            );
            pharmacistResponses.add(pharmacistResponse);
        }
        return ResponseEntity.ok(pharmacistResponses);
    }

}
