package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Pharmacist;
import com.andrew.pharmapay.repositories.PharmacistRepository;
import org.springframework.stereotype.Service;

@Service
public class PharmacistService {
    final PharmacistRepository pharmacistRepository;

    public PharmacistService(PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    public Pharmacist createPharamacist(Pharmacist pharmacist) {
        return pharmacistRepository.save(pharmacist);
    }
}
