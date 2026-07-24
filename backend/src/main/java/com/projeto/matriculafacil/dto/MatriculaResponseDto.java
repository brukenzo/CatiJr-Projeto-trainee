package com.projeto.matriculafacil.dto;

import java.util.UUID;

import com.projeto.matriculafacil.matricula.MatriculaModel;

public record MatriculaResponseDto(UUID matriculaID, UUID alunoID, UUID materiaID, String status) {

    public static MatriculaResponseDto from(MatriculaModel matricula) {
        return new MatriculaResponseDto(
                matricula.getMatriculaID(),
                matricula.getAlunoID(),
                matricula.getMateriaID(),
                matricula.getStatus()
        );
    }
}