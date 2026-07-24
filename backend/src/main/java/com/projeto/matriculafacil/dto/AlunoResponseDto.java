package com.projeto.matriculafacil.dto;

import java.util.UUID;
import com.projeto.matriculafacil.aluno.AlunoModel;

public record AlunoResponseDto (UUID alunoID, String nome, String email, int creditoDoSemestre){
    public static AlunoResponseDto from (AlunoModel aluno){
        return new AlunoResponseDto(
            aluno.getAlunoID(),
            aluno.getNome(),
            aluno.getEmail(),
            aluno.getCreditoDoSemestre()
        );
    }
}