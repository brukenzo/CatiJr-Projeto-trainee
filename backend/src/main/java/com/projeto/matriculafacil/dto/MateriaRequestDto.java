package com.projeto.matriculafacil.dto;

import com.projeto.matriculafacil.materia.MateriaModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record MateriaRequestDto(

        @NotBlank(message = "O código da matéria é obrigatório")
        String codigoMateria,

        @NotBlank(message = "O nome da matéria é obrigatório")
        String nome,

        @Positive(message = "O crédito deve ser maior que zero")
        int credito,

        @PositiveOrZero(message = "A quantidade de vagas não pode ser negativa")
        int qtdVagas,

        String horario,
        String preRequisito,
        String professor,
        String descricao
) {

    public MateriaModel toModel() {
        var materia = new MateriaModel();
        materia.setCodigoMateria(codigoMateria);
        materia.setNome(nome);
        materia.setCredito(credito);
        materia.setQtdVagas(qtdVagas);
        materia.setHorario(horario);
        materia.setPreRequisito(preRequisito);
        materia.setProfessor(professor);
        materia.setDescricao(descricao);
        return materia;
    }
}