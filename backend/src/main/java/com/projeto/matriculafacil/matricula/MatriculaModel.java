package com.projeto.matriculafacil.matricula;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="tb_matricula")
public class MatriculaModel {
    
    @Id
    @GeneratedValue(generator="UUID")
    private UUID matriculaID;

    private UUID alunoID;
    private UUID materiaID;

    // Status: disponível, indisponível, inscrita, concluída e reprovada
    private String status; 
}
