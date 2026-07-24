package com.projeto.matriculafacil.aluno;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.matriculafacil.dto.AlunoCadastroDto;
import com.projeto.matriculafacil.dto.AlunoLoginDto;
import com.projeto.matriculafacil.dto.AlunoResponseDto;
import com.projeto.matriculafacil.dto.LoginResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
    
    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponseDto> create(@Valid @RequestBody AlunoCadastroDto dto) {
        var alunoCriado = alunoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(AlunoResponseDto.from(alunoCriado));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody AlunoLoginDto dto) {
        var token = alunoService.login(dto);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
    
}