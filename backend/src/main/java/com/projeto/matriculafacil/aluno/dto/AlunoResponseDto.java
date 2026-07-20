package com.projeto.matriculafacil.aluno.dto;

import java.util.UUID;

public record AlunoResponseDto (UUID alunoID, String nome, String email, int creditoDoSemestre){}