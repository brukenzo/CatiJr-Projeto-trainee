package com.projeto.matriculafacil.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record MatriculaRequestDto(
    @NotBlank(message = "O ID da matéria é obrigatório")
    UUID materiaID
) {}