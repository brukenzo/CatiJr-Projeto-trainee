package com.projeto.matriculafacil.materia;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.matriculafacil.dto.MateriaRequestDto;
import com.projeto.matriculafacil.dto.MateriaResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/materia")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @PostMapping
    public ResponseEntity<MateriaResponseDto> create(@Valid @RequestBody MateriaRequestDto dto) {
        var materiaCriada = materiaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(MateriaResponseDto.from(materiaCriada));
    }

    @GetMapping
    public ResponseEntity<List<MateriaResponseDto>> listAll() {
        var materias = materiaService.listarTodas().stream()
                .map(MateriaResponseDto::from)
                .toList();

        return ResponseEntity.ok(materias);
    }
}