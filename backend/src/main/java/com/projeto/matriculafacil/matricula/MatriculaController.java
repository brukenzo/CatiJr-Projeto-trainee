package com.projeto.matriculafacil.matricula;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.matriculafacil.aluno.AlunoModel;
import com.projeto.matriculafacil.dto.MateriaResponseDto;
import com.projeto.matriculafacil.dto.MatriculaRequestDto;
import com.projeto.matriculafacil.dto.MatriculaResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/matricula")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping("/inscrever")
    public ResponseEntity<MatriculaResponseDto> inscrever(
            @Valid @RequestBody MatriculaRequestDto dto,
            @AuthenticationPrincipal AlunoModel aluno) {

        var matricula = matriculaService.inscrever(aluno, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(MatriculaResponseDto.from(matricula));
    }

    @GetMapping("/minhas-matriculas")
    public ResponseEntity<List<MateriaResponseDto>> listarMinhasMatriculas(
            @AuthenticationPrincipal AlunoModel aluno) {
        var materias = matriculaService.getMinhasMatriculas(aluno);
        return ResponseEntity.ok(materias);
    }
}