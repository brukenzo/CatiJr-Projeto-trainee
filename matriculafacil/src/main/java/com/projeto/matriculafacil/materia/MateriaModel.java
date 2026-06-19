package com.projeto.matriculafacil.materia;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_materia")
public class MateriaModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID materiaID;

    @Column(unique = true)
    private String codigoMateria;

    private String nome;
    private int credito;
    private int qtdVagas;
    private String horario;
    private String preRequisito;
    private String professor;
    private String descricao;
}
