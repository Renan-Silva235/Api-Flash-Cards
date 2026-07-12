package com.flashcards.api.services;

import com.flashcards.api.entities.VerificationCode;
import com.flashcards.api.enums.VerificationType;
import com.flashcards.api.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationService {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public void sendCode(String email, VerificationType type) {

        VerificationCode verificationCode =
                verificationCodeService.generate(email, type);

        emailService.sendVerificationCode(
                email,
                verificationCode.getCode()
        );
    }

    public void validateCode(String email, String code, VerificationType type) {

        VerificationCode verificationCode = verificationCodeRepository
                .findByEmailAndCodeAndType(email, code, type)
                .orElseThrow(() ->
                        new RuntimeException("Código inválido.")
                );

        if (verificationCode.isUsed()) {
            throw new RuntimeException("Este código já foi utilizado.");
        }

        if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado.");
        }
    }

    public void markAsUsed(String email, String code, VerificationType type) {

        VerificationCode verificationCode = verificationCodeRepository
                .findByEmailAndCodeAndType(email, code, type)
                .orElseThrow(() ->
                        new RuntimeException("Código inválido.")
                );

        verificationCode.setUsed(true);

        verificationCodeRepository.save(verificationCode);
    }
}