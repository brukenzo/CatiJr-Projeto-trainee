package com.projeto.matriculafacil.dto;

import java.util.UUID;

public record AlunoResponseDto (UUID alunoID, String nome, String email, int creditoDoSemestre){}