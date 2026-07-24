package com.projeto.matriculafacil.materia;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<MateriaModel, UUID> {
    Optional<MateriaModel> findByCodigoMateria(String codigoMateria);
}
