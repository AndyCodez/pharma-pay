package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PharmacistService {
    final PharmacistRepository pharmacistRepository;

    public PharmacistService(PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    public Pharmacist createPharmacist(Pharmacist pharmacist) {
        try {
            return pharmacistRepository.save(pharmacist);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already taken");
        }
    }
}
