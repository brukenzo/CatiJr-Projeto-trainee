package com.projeto.matriculafacil.aluno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.matriculafacil.aluno.dto.AlunoCadastroDto;
import com.projeto.matriculafacil.aluno.dto.AlunoLoginDto;
import com.projeto.matriculafacil.aluno.dto.AlunoResponseDto;

import com.projeto.matriculafacil.security.JwtService;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/aluno")
public class AlunoController {
    
    @Autowired
    private IAlunoRepository alunoRepository;

    @Autowired
    private JwtService jwtService;
    
    // Realiza o cadastro do aluno
    @PostMapping("/")
    public ResponseEntity create(@RequestBody AlunoCadastroDto cadastroDto){

        var email = this.alunoRepository.findByEmail(cadastroDto.email());

        if (email.isPresent()){ // O email já existe
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O email digitado já existe");
        }

        var alunoModel = new AlunoModel();
        alunoModel.setNome(cadastroDto.nome());
        alunoModel.setEmail(cadastroDto.email());
        alunoModel.setCreditoDoSemestre(0); // Aluno começa com 0 crédito
        
        // Realiza a criptografia da senha
        var senhaCriptografado = new BCryptPasswordEncoder().encode(cadastroDto.senha());
        alunoModel.setSenha(senhaCriptografado);

        var alunoCriado = this.alunoRepository.save(alunoModel);

        // Cria o DTO para response (sem senha)
        var responseDto = new AlunoResponseDto(
            alunoCriado.getAlunoID(),
            alunoCriado.getNome(),
            alunoCriado.getEmail(),
            alunoCriado.getCreditoDoSemestre()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Realiza o login do aluno
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AlunoLoginDto loginDto){

        var alunoOptional = this.alunoRepository.findByEmail(loginDto.email());

        if (!alunoOptional.isPresent()){ // O email digitado não foi encontrado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("O email ou a senha estão incorretos");
        }

        var aluno = alunoOptional.get();

        var senhaValida = new BCryptPasswordEncoder().matches(loginDto.senha(), aluno.getSenha());

        if (!senhaValida){ // A senha digitada está incorreta
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("O email ou a senha estão incorretos");
        }

        // Gera o token
        var token = jwtService.generateToken(aluno.getAlunoID().toString());
        return ResponseEntity.ok(token);
    }   
}