package com.projeto.matriculafacil.matricula;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMatriculaRepository extends JpaRepository<MatriculaModel, UUID> {
    // Devolve a lista de matérias que um aluno está ou foi inscrito
    List<MatriculaModel> findByAlunoID(UUID alunoId);
    // Devolve se o aluno já foi registrado alguma vez na matéria
    Optional<MatriculaModel> findByAlunoIDAndMateriaID(UUID alunoID, UUID materiaID);
}
