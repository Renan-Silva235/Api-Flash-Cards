package com.flashcards.api.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String code,

        @NotBlank
        @Size(min = 6, message = "A senha deve possuir no mínimo 6 caracteres.")
        String newPassword

) {
}