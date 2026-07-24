package com.projeto.matriculafacil.matricula;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.matriculafacil.materia.MateriaRepository;
import com.projeto.matriculafacil.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.projeto.matriculafacil.dto.MatriculaRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/matricula")
public class MatriculaController {
    
    @Autowired
    private IMatriculaRepository matriculaRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/inscrever")
    public ResponseEntity inscrever(@RequestBody MatriculaRequestDto requestDto, HttpServletRequest request){
        
        // Extrai o ID do aluno pelo cabeçalho do token
        var token = request.getHeader("Authorization");
        var alunoID = UUID.fromString(jwtService.validateToken(token));

        // Verifica se a matéria está cadastrada no banco de dados
        var materiaOptional = this.materiaRepository.findById(requestDto.materiaID());
        if (!materiaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Matéria não encontrada no catálogo");
        }

        var materiaPuxada = materiaOptional.get();

        // verifica se o aluno tem histórico nessa matéria
        var matriculaExistente = this.matriculaRepository.findByAlunoIDAndMateriaID(alunoID, materiaPuxada.getMateriaID());
        if (matriculaExistente.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Você está inscrito ou já estava inscrito nesta disciplina");
        }

        // Verifica se a matéria tem pré requisito
        String preReq = materiaPuxada.getPreRequisito();
        if (!preReq.equals("NENHUM")){
            var materiaPreReq = this.materiaRepository.findByCodigoMateria(preReq).get();

            var matriculaPreReq = this.matriculaRepository.findByAlunoIDAndMateriaID(alunoID, materiaPreReq.getMateriaID());

            if (!matriculaPreReq.isPresent() || !matriculaPreReq.get().getStatus().equals("CONCLUIDA")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Você precisa concluir " + preReq + " antes de cursar esta matéria.");
            }
        }

        // Verifica se causa conflito de horário
        var historicoMatricula = this.matriculaRepository.findByAlunoID(alunoID);
        int somaCreditos = 0;
        for (MatriculaModel matricula : historicoMatricula){
            if (matricula.getStatus().equals("INSCRITA")){
                var materiaInscrita = this.materiaRepository.findById(matricula.getMateriaID()).get();
                somaCreditos+=materiaInscrita.getCredito();

                if (temConflito(materiaInscrita.getHorario(), materiaPuxada.getHorario())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Conflito de Horário: A disciplina " + materiaPuxada.getNome() + " entra em choque com " + materiaInscrita.getNome());
                }
            }
        }
        // Verifica se pegar essa matéria não ultrapassa o limite de 24 créditos
        if (somaCreditos + materiaPuxada.getCredito() > 24){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Limite de 24 créditos por semestre ultrapassado");
        }
        
        // Salva a matrícula como inscrita
        var novaMatricula = new MatriculaModel();
        novaMatricula.setAlunoID(alunoID);
        novaMatricula.setMateriaID(materiaPuxada.getMateriaID());
        novaMatricula.setStatus("INSCRITA");

        var matriculaSalva = this.matriculaRepository.save(novaMatricula);

        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaSalva);
    }
    
    private boolean temConflito(String horario1, String horario2){
        // Separa a string em blocos
        // Exemplo: "Seg 08: - 12:00, Qui 16:00 - 18:00" -> {"Seg 08:00 - 12:00", "Qui 16:00 - 18:00"}
        String[] bloco1 = horario1.split(",");
        String[] bloco2 = horario2.split(",");

        for (String b1 : bloco1){
            for (String b2 : bloco2){
                // Separa a string em 4 partes
                // Exemplo: "Seg 08:00 - 12:00" -> {"Seg", "08:00", "-", "12:00"}
                String[] parteHorario1 = b1.trim().split(" ");
                String[] parteHorario2 = b2.trim().split(" ");

                // Se os dias da semana são diferentes, não causam conflito
                if (!parteHorario1[0].equalsIgnoreCase(parteHorario2[0])){
                    continue;
                }
                
                // Converte as strings em horas
                java.time.LocalTime horarioInicio1 = java.time.LocalTime.parse(parteHorario1[1]);
                java.time.LocalTime horarioFim1 = java.time.LocalTime.parse(parteHorario1[3]);
                java.time.LocalTime horarioInicio2 = java.time.LocalTime.parse(parteHorario2[1]);
                java.time.LocalTime horarioFim2 = java.time.LocalTime.parse(parteHorario2[3]);
                
                // Verifica se causa conflito
                if (horarioInicio1.isBefore(horarioFim2) && horarioFim1.isAfter(horarioInicio2)){
                    return true;
                }
            }
        }
        return false;
    }
}

