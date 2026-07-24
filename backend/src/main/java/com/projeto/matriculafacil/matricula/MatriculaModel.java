package com.projeto.matriculafacil.matricula;

import java.util.UUID;

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
@Table(name="tb_matricula")
public class MatriculaModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID matriculaID;

    private UUID alunoID;
    private UUID materiaID;

    // Status: inscrita, concluída e reprovada
    private String status; 
}
