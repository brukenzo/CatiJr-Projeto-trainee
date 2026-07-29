package com.projeto.matriculafacil.matricula;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projeto.matriculafacil.aluno.AlunoModel;
import com.projeto.matriculafacil.aluno.AlunoRepository;
import com.projeto.matriculafacil.dto.MateriaResponseDto;
import com.projeto.matriculafacil.dto.MatriculaRequestDto;
import com.projeto.matriculafacil.dto.MatriculaResponseDto;
import com.projeto.matriculafacil.exception.RecursoNaoEncontradoException;
import com.projeto.matriculafacil.exception.RegraDeNegocioException;
import com.projeto.matriculafacil.materia.MateriaModel;
import com.projeto.matriculafacil.materia.MateriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private static final String STATUS_INSCRITA = "INSCRITA";
    private static final String STATUS_CONCLUIDA = "CONCLUIDA";
    private static final String SEM_PRE_REQUISITO = "NENHUM";
    private static final int LIMITE_CREDITOS_SEMESTRE = 24;

    private final MatriculaRepository matriculaRepository;
    private final MateriaRepository materiaRepository;
    private final AlunoRepository alunoRepository;

    // Método para inscrever aluno em uma matéria
    public MatriculaModel inscrever(AlunoModel aluno, MatriculaRequestDto dto) {
        var materia = materiaRepository.findById(dto.materiaID())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada no catálogo"));

        if (!materia.isDisponivel()) {
            throw new RegraDeNegocioException("Esta matéria não está disponível neste semestre");
        }

        var existe = matriculaRepository.findByAlunoIDAndMateriaID(aluno.getAlunoID(), materia.getMateriaID());
        if (existe.isPresent()) {
            String status = existe.get().getStatus();
            if (status.equals(STATUS_CONCLUIDA))
                throw new RegraDeNegocioException("Você já concluiu esta disciplina");
            else if (status.equals(STATUS_INSCRITA))
                throw new RegraDeNegocioException("Você já está inscrito nesta disciplina");
        }

        validarPreRequisito(aluno, materia);
        validarConflitoDeHorarioELimiteDeCreditos(aluno, materia);

        // Se o aluno já tem matrícula na matéria (aluno ficou reprovado) apenas atualiza, se não cria
        var matricula = existe.orElseGet(() -> {
            var nova = new MatriculaModel();
            nova.setAlunoID(aluno.getAlunoID());
            nova.setMateriaID(materia.getMateriaID());
            return nova;
        });
        matricula.setStatus(STATUS_INSCRITA);

        aluno.setCreditoDoSemestre(aluno.getCreditoDoSemestre() + materia.getCredito());
        alunoRepository.save(aluno);

        return matriculaRepository.save(matricula);
    }
    
    private void validarPreRequisito(AlunoModel aluno, MateriaModel materia) {
        var preRequisito = materia.getPreRequisito();

        if (SEM_PRE_REQUISITO.equals(preRequisito)) {
            return;
        }

        var materiaPreRequisito = materiaRepository.findByCodigoMateria(preRequisito)
                .orElseThrow(() -> new RegraDeNegocioException("Pré-requisito " + preRequisito + " não encontrado no catálogo"));

        var matriculaPreRequisito = matriculaRepository.findByAlunoIDAndMateriaID(
                aluno.getAlunoID(), materiaPreRequisito.getMateriaID());

        if (matriculaPreRequisito.isEmpty() || !STATUS_CONCLUIDA.equals(matriculaPreRequisito.get().getStatus())) {
            throw new RegraDeNegocioException("Você precisa concluir " + preRequisito + " antes de cursar esta matéria");
        }
    }
    
    private void validarConflitoDeHorarioELimiteDeCreditos(AlunoModel aluno, MateriaModel materiaDesejada) {
        var historico = matriculaRepository.findByAlunoID(aluno.getAlunoID());

        int somaCreditos = 0;
        for (MatriculaModel matricula : historico) {
            if (!STATUS_INSCRITA.equals(matricula.getStatus())) {
                continue;
            }

            var materiaInscrita = materiaRepository.findById(matricula.getMateriaID())
                    .orElseThrow(() -> new RegraDeNegocioException("Matéria do histórico não encontrada no catálogo"));

            somaCreditos += materiaInscrita.getCredito();

            if (temConflito(materiaInscrita.getHorario(), materiaDesejada.getHorario())) {
                throw new RegraDeNegocioException(
                        "Conflito de horário: a disciplina " + materiaDesejada.getNome()
                                + " entra em choque com " + materiaInscrita.getNome());
            }
        }

        if (somaCreditos + materiaDesejada.getCredito() > LIMITE_CREDITOS_SEMESTRE) {
            throw new RegraDeNegocioException("Limite de " + LIMITE_CREDITOS_SEMESTRE + " créditos por semestre ultrapassado");
        }
    }

    // Verifica se dois horários se sobrepõem
    // Horário está no formato: "Seg 08:00 - 12:00, Qui 14:00 - 16:00"
    private boolean temConflito(String horario1, String horario2) {
        // Separa o horário se tiver vírugla
        // "Seg 08:00 - 12:00, Qui 14:00 - 16:00" -> "Seg 08:00 - 12:00", "Qui 14:00 - 16:00"
        String[] bloco1 = horario1.split(",");
        String[] bloco2 = horario2.split(",");

        for (String b1 : bloco1) {
            for (String b2 : bloco2) {
                // Separa o horário em partes
                // "Seg 08:00 - 12:00" -> {"Seg", "08:00", "-", "12:00"}
                String[] parteHorario1 = b1.trim().split(" ");
                String[] parteHorario2 = b2.trim().split(" ");

                // Se os dias da semana forem diferentes, não tem como ter conflito de horário
                if (!parteHorario1[0].equalsIgnoreCase(parteHorario2[0])) {
                    continue;
                }

                LocalTime inicio1 = LocalTime.parse(parteHorario1[1]);
                LocalTime fim1 = LocalTime.parse(parteHorario1[3]);
                LocalTime inicio2 = LocalTime.parse(parteHorario2[1]);
                LocalTime fim2 = LocalTime.parse(parteHorario2[3]);

                // Verifica se os horários se conflitam
                if (inicio1.isBefore(fim2) && fim1.isAfter(inicio2)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Método para devolver a lista de matérias inscritas
    public List<MateriaResponseDto> getMinhasMatriculas(AlunoModel aluno) {
        var historico = matriculaRepository.findByAlunoID(aluno.getAlunoID());

        List<UUID> idMateriaInscrita = new ArrayList<>();
        for (MatriculaModel matricula : historico) {
            if (STATUS_INSCRITA.equals(matricula.getStatus())) {
                idMateriaInscrita.add(matricula.getMateriaID());
            }
        }

        // Se não tem matérias inscrita retorna vazio
        if (idMateriaInscrita.isEmpty()) {
            return List.of();
        }

        var materias = materiaRepository.findAllById(idMateriaInscrita);

        List<MateriaResponseDto> minhasMatriculas = new ArrayList<>();
        for (MateriaModel materia : materias) {
            minhasMatriculas.add(MateriaResponseDto.from(materia)); 
        }

        return minhasMatriculas;
    }

    public List<MatriculaResponseDto> getHistorico(AlunoModel aluno) {
        return matriculaRepository.findByAlunoID(aluno.getAlunoID())
                .stream()
                .map(MatriculaResponseDto::from)
                .toList();
    }

    // Método para desmatricular aluno de uma matéria
    public void desinscrever (AlunoModel aluno, UUID materiaId){
        var matricula = matriculaRepository.findByAlunoIDAndMateriaID(aluno.getAlunoID(), materiaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada"));
        
        if (!STATUS_INSCRITA.equals(matricula.getStatus())){
            throw new RegraDeNegocioException("Só é possível cancelar matrícula em matérias que você está inscrito");
        }
        
        var materia = materiaRepository.findById(materiaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada"));

        aluno.setCreditoDoSemestre(aluno.getCreditoDoSemestre() - materia.getCredito()); 
        alunoRepository.save(aluno);

        matriculaRepository.delete(matricula);
    }
}