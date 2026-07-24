package com.projeto.matriculafacil.materia;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.matriculafacil.dto.MateriaRequestDto;
import com.projeto.matriculafacil.exception.RegraDeNegocioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;

    // Método para cadastrar uma matéria nova
    public MateriaModel cadastrar(MateriaRequestDto dto) {

        if (materiaRepository.findByCodigoMateria(dto.codigoMateria()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma matéria cadastrada com este código");
        }

        return materiaRepository.save(dto.toModel());
    }

    // Método para retornar a lista com todas as matérias cadastradas
    public List<MateriaModel> listarTodas() {
        return materiaRepository.findAll();
    }
}