package com.projeto.matriculafacil.dto;

import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaRequestDto (
    @NotBlank(message = "O email é obrigatório")
    String email
)
{}
