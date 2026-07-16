package com.flashcards.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${SMTP_MAIL_EMAIL_CORPORATION}")
    private String smtpMailCorporation;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(smtpMailCorporation);
        message.setTo(to);
        message.setSubject("Código de verificação - Flash Cards");
        message.setText("""
            Seu código de verificação é:
            %s
            Este código expira em 7 minutos.
            Se você não solicitou esta operação, ignore este e-mail.
            """.formatted(code));
        mailSender.send(message);
    }
}