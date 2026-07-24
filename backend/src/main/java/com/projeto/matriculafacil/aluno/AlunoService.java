package com.projeto.matriculafacil.aluno;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.matriculafacil.dto.AlunoCadastroDto;
import com.projeto.matriculafacil.dto.AlunoLoginDto;
import com.projeto.matriculafacil.exception.CredenciaisInvalidasException;
import com.projeto.matriculafacil.exception.RegraDeNegocioException;
import com.projeto.matriculafacil.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // Método para cadastrar aluno
    public AlunoModel cadastrar(AlunoCadastroDto dto) {

        if (alunoRepository.findByEmail(dto.email()).isPresent()) {
            throw new RegraDeNegocioException("O email digitado já existe");
        }

        var aluno = new AlunoModel();
        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setCreditoDoSemestre(0); // O aluno inicia com 0 créditos
        aluno.setSenha(passwordEncoder.encode(dto.senha()));

        return alunoRepository.save(aluno);
    }

    // Método para realizar login do aluno
    public String login(AlunoLoginDto dto) {

        var aluno = alunoRepository.findByEmail(dto.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(dto.senha(), aluno.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        return jwtService.generateToken(aluno.getAlunoID().toString());
    }
}