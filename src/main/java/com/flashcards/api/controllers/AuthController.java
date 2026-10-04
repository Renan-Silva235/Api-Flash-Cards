package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.ChangePasswordDTO;
import com.flashcards.api.dtos.request.LoginRequestDTO;
import com.flashcards.api.dtos.request.SendVerificationCodeDTO;
import com.flashcards.api.dtos.request.VerifyCodeDTO;
import com.flashcards.api.dtos.response.LoginResponseDTO;
import com.flashcards.api.dtos.response.UserResponseDTO;
import com.flashcards.api.enums.VerificationType;
import com.flashcards.api.security.CurrentUserService;
import com.flashcards.api.security.jwt.JwtService;
import com.flashcards.api.security.userDetails.CustomUserDetails;
import com.flashcards.api.services.AuthService;
import com.flashcards.api.services.EmailService;
import com.flashcards.api.services.UserService;
import com.flashcards.api.services.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CurrentUserService currentUserService;

    // Cookie do login (guarda o JWT).
    // Secure: o navegador só envia o cookie por HTTPS. O padrão é SEGURO (true), então a
    // produção já nasce protegida mesmo sem configurar nada. Para rodar localmente em HTTP,
    // coloque COOKIE_SECURE=false no .env.
    // SameSite: Lax funciona porque o front chama a API pelo mesmo site (rewrite /api na Vercel).
    @Value("${COOKIE_SECURE:true}")
    private boolean cookieSecure;

    @Value("${COOKIE_SAME_SITE:Lax}")
    private String cookieSameSite;
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
        ResponseCookie jwtCookie = buildJwtCookie(token, 24 * 60 * 60); // 1 dia

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


    // Devolve o usuário dono do token (cookie na Web, header Authorization no Mobile).
    // O front chama ao abrir o app para restaurar o login depois de recarregar a página.
    // Sem token válido, o filtro JWT / Spring Security responde 401.
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me() {
        return ResponseEntity.ok(new UserResponseDTO(currentUserService.getCurrentUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        // Sobrescreve o cookie com um valor vazio e maxAge 0, fazendo o navegador apagá-lo
        ResponseCookie jwtCookie = buildJwtCookie("", 0);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }

    @PostMapping("/password/send-code")
    public ResponseEntity<Void> sendPasswordCode(
            @RequestBody @Valid SendVerificationCodeDTO dto
    ) {
        // Segurança: a resposta é sempre a mesma, exista ou não uma conta com esse e-mail,
        // para não revelar quais e-mails estão cadastrados (enumeração de usuários).
        // O envio roda em segundo plano para o tempo de resposta também não denunciar.
        if (userService.emailExists(dto.email())) {
            CompletableFuture.runAsync(() -> {
                try {
                    verificationService.sendCode(
                            dto.email(),
                            VerificationType.CHANGE_PASSWORD
                    );
                } catch (Exception e) {
                    System.err.println("Erro ao enviar código de redefinição de senha: " + e.getMessage());
                }
            });
        }

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

    // Login e logout precisam gerar o cookie com os MESMOS atributos,
    // senão o navegador não reconhece como o mesmo cookie e não apaga no logout
    private ResponseCookie buildJwtCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from("jwt_token", value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite(cookieSameSite)
                .build();
    }
}
