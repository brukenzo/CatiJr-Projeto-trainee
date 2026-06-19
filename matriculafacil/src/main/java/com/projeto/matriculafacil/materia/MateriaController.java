package com.projeto.matriculafacil.materia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/materia")
public class MateriaController {
    
    @Autowired
    private IMateriaRepository materiaRepository;

    // Lógica para cadastrar uma matéria
    @PostMapping("/")
    public ResponseEntity create(@RequestBody MateriaModel materiaModel){
        var materia = this.materiaRepository.findByCodigoMateria(materiaModel.getCodigoMateria());

        if (materia.isPresent()){ // A matéria já está cadastrada
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe uma matéria cadastrada com este código");
        }

        var materiaCriada = this.materiaRepository.save(materiaModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(materiaCriada);
    }
}
