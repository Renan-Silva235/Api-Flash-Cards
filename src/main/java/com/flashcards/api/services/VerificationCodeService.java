package com.flashcards.api.services;

import com.flashcards.api.entities.VerificationCode;
import com.flashcards.api.enums.VerificationType;
import com.flashcards.api.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeService {

    @Autowired
    private VerificationCodeRepository repository;

    private static final int EXPIRATION_MINUTES = 50    ;

    public VerificationCode generate(String email, VerificationType type) {

        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setEmail(email);
        verificationCode.setType(type);
        verificationCode.setCode(generateCode());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
        verificationCode.setUsed(false);

        return repository.save(verificationCode);
    }

    private String generateCode() {
        Random random = new Random();

        return String.format("%06d", random.nextInt(1000000));
    }
}