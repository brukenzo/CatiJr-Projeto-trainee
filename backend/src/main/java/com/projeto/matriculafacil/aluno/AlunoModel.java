package com.projeto.matriculafacil.aluno;

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
@Table(name = "tb_aluno")
public class AlunoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID alunoID;

    @Column(unique = true) // Permite um único email
    private String email;
    
    private String nome;
    private String senha;
    private int creditoDoSemestre;
}
