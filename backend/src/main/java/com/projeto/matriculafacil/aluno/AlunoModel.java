package com.projeto.matriculafacil.aluno;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_aluno")
public class AlunoModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID alunoID;

    @Column(unique = true)
    private String email;
    
    private String nome;
    private String senha;
    private int creditoDoSemestre;
}
