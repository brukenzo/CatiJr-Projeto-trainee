package com.projeto.matriculafacil.matricula;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.projeto.matriculafacil.aluno.AlunoModel;
import com.projeto.matriculafacil.dto.MatriculaRequestDto;
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

    public MatriculaModel inscrever(AlunoModel aluno, MatriculaRequestDto dto) {

        var materia = materiaRepository.findById(dto.materiaID())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada no catálogo"));

        if (matriculaRepository.findByAlunoIDAndMateriaID(aluno.getAlunoID(), materia.getMateriaID()).isPresent()) {
            throw new RegraDeNegocioException("Você está inscrito ou já estava inscrito nesta disciplina");
        }

        validarPreRequisito(aluno, materia);
        validarConflitoDeHorarioELimiteDeCreditos(aluno, materia);

        var novaMatricula = new MatriculaModel();
        novaMatricula.setAlunoID(aluno.getAlunoID());
        novaMatricula.setMateriaID(materia.getMateriaID());
        novaMatricula.setStatus(STATUS_INSCRITA);

        return matriculaRepository.save(novaMatricula);
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

    // Verifica se dois horários (formato "Seg 08:00 - 12:00, Qui 14:00 - 16:00") se sobrepõem
    private boolean temConflito(String horario1, String horario2) {
        String[] bloco1 = horario1.split(",");
        String[] bloco2 = horario2.split(",");

        for (String b1 : bloco1) {
            for (String b2 : bloco2) {
                String[] parteHorario1 = b1.trim().split(" ");
                String[] parteHorario2 = b2.trim().split(" ");

                if (!parteHorario1[0].equalsIgnoreCase(parteHorario2[0])) {
                    continue;
                }

                LocalTime inicio1 = LocalTime.parse(parteHorario1[1]);
                LocalTime fim1 = LocalTime.parse(parteHorario1[3]);
                LocalTime inicio2 = LocalTime.parse(parteHorario2[1]);
                LocalTime fim2 = LocalTime.parse(parteHorario2[3]);

                if (inicio1.isBefore(fim2) && fim1.isAfter(inicio2)) {
                    return true;
                }
            }
        }
        return false;
    }
}