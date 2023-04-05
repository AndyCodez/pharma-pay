package com.andrew.pharmapay.repositories;

import com.andrew.pharmapay.models.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    Optional<UserDetails> findByEmail(String email);
}
