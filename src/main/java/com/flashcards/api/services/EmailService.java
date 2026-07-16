package com.flashcards.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    @Value("${SMTP_MAIL_EMAIL_CORPORATION}")
    private String senderEmail;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendVerificationCode(String to, String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);
        headers.set("accept", "application/json");

        Map<String, Object> sender = new HashMap<>();
        sender.put("email", senderEmail);
        sender.put("name", "Flash Cards");

        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", to);

        String htmlContent = """
            <html><body>
            <p>Seu código de verificação é: <strong>%s</strong></p>
            <p>Este código expira em 7 minutos.</p>
            <p>Se você não solicitou esta operação, ignore este e-mail.</p>
            </body></html>
            """.formatted(code);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", List.of(recipient));
        body.put("subject", "Código de verificação - Flash Cards");
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(BREVO_API_URL, request, String.class);
    }
}