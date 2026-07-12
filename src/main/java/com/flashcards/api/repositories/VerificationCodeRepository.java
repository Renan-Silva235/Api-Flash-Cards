package com.flashcards.api.repositories;

import com.flashcards.api.entities.VerificationCode;
import com.flashcards.api.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {

    Optional<VerificationCode> findFirstByEmailAndTypeAndUsedFalseOrderByExpiresAtDesc(
            String email,
            VerificationType type
    );

    Optional<VerificationCode> findByEmailAndCodeAndTypeAndUsedFalse(
            String email,
            String code,
            VerificationType type
    );

    Optional<VerificationCode> findByEmailAndCodeAndType(
            String email,
            String code,
            VerificationType type
    );
}