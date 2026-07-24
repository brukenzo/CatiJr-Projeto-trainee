package com.projeto.matriculafacil.materia;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_materia")
public class MateriaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID materiaID;

    @Column(unique = true) 
    private String codigoMateria; // Permite um único código de matéria

    private String nome;
    private int credito;
    private int qtdVagas;
    private String horario;
    private String preRequisito;
    private String professor;
    private String descricao;
}