package com.projeto.matriculafacil.dto;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRequestDto(
    @NotBlank(message = "O email é obrigatório")
    String email,

    @NotBlank(message = "O código é obrigatório")
    String codigo,

    @NotBlank(message = "A nova senha é obrigatória")
    String novaSenha
) {}