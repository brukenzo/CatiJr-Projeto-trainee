package com.projeto.matriculafacil.dto;

import jakarta.validation.constraints.NotBlank;

public record AlunoLoginDto(

    @NotBlank(message = "O email é obrigatório")
    String email, 
    
    @NotBlank(message = "A senha é obrigatória")
    String senha
) {}