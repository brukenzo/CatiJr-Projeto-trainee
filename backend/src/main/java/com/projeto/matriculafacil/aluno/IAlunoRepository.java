package com.projeto.matriculafacil.aluno;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlunoRepository extends JpaRepository<AlunoModel, UUID> {
    
    Optional<AlunoModel> findByEmail(String email);
} 
