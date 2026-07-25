package com.projeto.matriculafacil.aluno;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.matriculafacil.dto.AlunoCadastroDto;
import com.projeto.matriculafacil.dto.AlunoLoginDto;
import com.projeto.matriculafacil.dto.EsqueciSenhaRequestDto;
import com.projeto.matriculafacil.dto.EsqueciSenhaResponseDto;
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

    // Método para enviar o código de recuperação
    public void enviarCodigoValidacao(EsqueciSenhaRequestDto dto) {

        var aluno = alunoRepository.findByEmail(dto.email())
            .orElseThrow(CredenciaisInvalidasException::new);
        
        // Gera um código aleatório
        Random random = new Random();
        int codigoAleatorio = random.nextInt(900000) + 100000;
        String codigo = String.valueOf(codigoAleatorio);
        
        aluno.setCodigoRecuperacao(codigo);
        alunoRepository.save(aluno);

        // Imprime o código no terminal para o usuário digitar como confirmação
        System.out.println("----------------------------------------");
        System.out.println("Código de recuperação: " + codigo);
        System.out.println("----------------------------------------");
    }

    // Método para validar o código e alterar a senha
    public void redefinirSenha(EsqueciSenhaResponseDto dto) {
        
        var aluno = alunoRepository.findByEmail(dto.email())
            .orElseThrow(CredenciaisInvalidasException::new);
        
        // Verifica se o código digitado é o mesmo que foi enviado
        if (!dto.codigo().equals(aluno.getCodigoRecuperacao())){
            throw new RegraDeNegocioException("Código de verificação incorreto ou expirado!");
        }

        aluno.setSenha(passwordEncoder.encode(dto.novaSenha()));

        // Apaga o código do banco de dados
        aluno.setCodigoRecuperacao(null);

        alunoRepository.save(aluno);
    }
}