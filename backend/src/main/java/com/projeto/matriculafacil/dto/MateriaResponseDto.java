package com.projeto.matriculafacil.dto;

import java.util.UUID;

import com.projeto.matriculafacil.materia.MateriaModel;

public record MateriaResponseDto(
        UUID materiaID,
        String codigoMateria,
        String nome,
        int credito,
        int qtdVagas,
        String horario,
        String preRequisito,
        String professor,
        String descricao
) {

    public static MateriaResponseDto from(MateriaModel materia) {
        return new MateriaResponseDto(
                materia.getMateriaID(),
                materia.getCodigoMateria(),
                materia.getNome(),
                materia.getCredito(),
                materia.getQtdVagas(),
                materia.getHorario(),
                materia.getPreRequisito(),
                materia.getProfessor(),
                materia.getDescricao()
        );
    }
}