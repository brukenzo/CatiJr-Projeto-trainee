package com.projeto.matriculafacil.aluno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
    
    @Autowired
    private IAlunoRepository alunoRepository;

    // Realiza o cadastro do aluno
    @PostMapping("/")
    public ResponseEntity create(@RequestBody AlunoModel alunoModel){

        var email = this.alunoRepository.findByEmail(alunoModel.getEmail());

        if (email.isPresent()){ // O email já existe
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O email digitado já existe");
        }
        
        // Realiza a criptografia da senha
        var senhaCriptografado = new BCryptPasswordEncoder().encode(alunoModel.getSenha());
        alunoModel.setSenha(senhaCriptografado);

        var alunoCriado = this.alunoRepository.save(alunoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoCriado);
    }
}
