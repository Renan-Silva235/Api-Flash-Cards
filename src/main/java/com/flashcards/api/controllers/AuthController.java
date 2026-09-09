package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.ChangePasswordDTO;
import com.flashcards.api.dtos.request.LoginRequestDTO;
import com.flashcards.api.dtos.request.SendVerificationCodeDTO;
import com.flashcards.api.dtos.request.VerifyCodeDTO;
import com.flashcards.api.dtos.response.LoginResponseDTO;
import com.flashcards.api.dtos.response.UserResponseDTO;
import com.flashcards.api.enums.VerificationType;
import com.flashcards.api.security.jwt.JwtService;
import com.flashcards.api.security.userDetails.CustomUserDetails;
import com.flashcards.api.services.AuthService;
import com.flashcards.api.services.EmailService;
import com.flashcards.api.services.UserService;
import com.flashcards.api.services.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {

        // 1. Autentica o usuário e gera o token JWT
        Authentication authentication = authService.authenticate(dto);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(authentication);

        // 2. Cria o cookie HttpOnly (Será ignorado pelo Mobile, mas usado pela Web)
        ResponseCookie jwtCookie = ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(true) // Lembre de mudar para false se testar localmente em HTTP na Web
                .path("/")
                .maxAge(24 * 60 * 60) // 1 dia
                .sameSite("Lax")
                .build();

        // 3. Monta o corpo da resposta contendo o token (Para o Mobile ler do JSON)
        LoginResponseDTO responseBody = new LoginResponseDTO(
                token,
                "Bearer",
                new UserResponseDTO(userDetails.getUser())
        );

        // 4. Retorna o Cookie no Header E o Token no Body
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(responseBody);
    }



    @PostMapping("/password/send-code")
    public ResponseEntity<Void> sendPasswordCode(
            @RequestBody @Valid SendVerificationCodeDTO dto
    ) {
        userService.validateEmailExists(dto.email());
        verificationService.sendCode(
                dto.email(),
                VerificationType.CHANGE_PASSWORD
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/verify-code")
    public ResponseEntity<Void> verifyCode(
            @RequestBody @Valid VerifyCodeDTO dto
    ) {

        verificationService.validateCode(
                dto.email(),
                dto.code(),
                VerificationType.CHANGE_PASSWORD
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordDTO dto
    ) {

        verificationService.validateCode(
                dto.email(),
                dto.code(),
                VerificationType.CHANGE_PASSWORD
        );

        userService.changePassword(
                dto.email(),
                dto.newPassword()
        );

        verificationService.markAsUsed(
                dto.email(),
                dto.code(),
                VerificationType.CHANGE_PASSWORD
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/send-code")
    public ResponseEntity<Void> sendRegisterCode(
            @RequestBody @Valid SendVerificationCodeDTO dto
    ) {
        userService.validateEmailAvailability(dto.email());
        verificationService.sendCode(
                dto.email(),
                VerificationType.REGISTER
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/verify-code")
    public ResponseEntity<Void> verifyRegisterCode(
            @RequestBody @Valid VerifyCodeDTO dto
    ) {

        verificationService.validateCode(
                dto.email(),
                dto.code(),
                VerificationType.REGISTER
        );

        return ResponseEntity.ok().build();
    }
}
