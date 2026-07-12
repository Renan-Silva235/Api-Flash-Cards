package com.flashcards.api.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private org.springframework.core.env.Environment environment;
    private final Dotenv dotenv = Dotenv.load();


    public void sendVerificationCode(String to, String code) {
        String smtpMailCorporation = dotenv.get("SMTP_MAIL_EMAIL_CORPORATION");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(smtpMailCorporation);
        message.setTo(to);
        message.setSubject("Código de verificação - Flash Cards");

        message.setText("""
            Seu código de verificação é:

            %s

            Este código expira em 3 minutos.

            Se você não solicitou esta operação, ignore este e-mail.
            """.formatted(code));

        mailSender.send(message);
    }
}