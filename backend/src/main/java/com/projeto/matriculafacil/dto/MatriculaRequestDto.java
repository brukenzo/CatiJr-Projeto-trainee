package com.projeto.matriculafacil.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MatriculaRequestDto(
    @NotNull(message = "O ID da matéria é obrigatório")
    UUID materiaID
) {}