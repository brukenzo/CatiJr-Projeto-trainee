package com.projeto.matriculafacil.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.matriculafacil.matricula.MatriculaModel;
import com.projeto.matriculafacil.aluno.AlunoModel;
import com.projeto.matriculafacil.aluno.AlunoRepository;
import com.projeto.matriculafacil.materia.MateriaRepository;
import com.projeto.matriculafacil.materia.MateriaModel;
import com.projeto.matriculafacil.matricula.IMatriculaRepository;

// Inicializa o banco de dados com algumas matérias
@Configuration
public class DataInitializerSeed implements CommandLineRunner{
    
    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private IMatriculaRepository matriculaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (this.materiaRepository.count() == 0) {

            // Cálculo 1
            var calc1 = new MateriaModel();
            calc1.setCodigoMateria("MAT101");
            calc1.setNome("Cálculo 1");
            calc1.setCredito(6);
            calc1.setQtdVagas(40);
            calc1.setHorario("Ter 08:00 - 10:00, Qui 14:00 - 18:00");
            calc1.setPreRequisito("NENHUM");
            calc1.setProfessor("Bruna");
            calc1.setDescricao("Limites, derivadas e integrais.");
            this.materiaRepository.save(calc1);

            // Cálculo 2 
            var calc2 = new MateriaModel();
            calc2.setCodigoMateria("MAT102");
            calc2.setNome("Cálculo 2");
            calc2.setCredito(6);
            calc2.setQtdVagas(30);
            calc2.setHorario("Qua 08:00 - 12:00");
            calc2.setPreRequisito("MAT101"); 
            calc2.setProfessor("Fábio");
            calc2.setDescricao("Derivadas parciais e séries.");
            this.materiaRepository.save(calc2);

            // Cálculo 3 
            var calc3 = new MateriaModel();
            calc3.setCodigoMateria("MAT103");
            calc3.setNome("Cálculo 3");
            calc3.setCredito(6);
            calc3.setQtdVagas(30);
            calc3.setHorario("Seg 08:00 - 10:00, Sex 10:00 - 12:00");
            calc3.setPreRequisito("MAT102"); 
            calc3.setProfessor("Cláudia");
            calc3.setDescricao("Integrais com múltiplas variáveis.");
            this.materiaRepository.save(calc3);

            // AED 1 
            var aed1 = new MateriaModel();
            aed1.setCodigoMateria("COMP101");
            aed1.setNome("Algoritmos e Estrutura de Dados 1");
            aed1.setCredito(8);
            aed1.setQtdVagas(60);
            aed1.setHorario("Seg 08:00 - 12:00");
            aed1.setPreRequisito("NENHUM");
            aed1.setProfessor("Mário");
            aed1.setDescricao("Ordenação e árvores.");
            this.materiaRepository.save(aed1);

            // Física 1 
            var fis1 = new MateriaModel();
            fis1.setCodigoMateria("FIS101");
            fis1.setNome("Física 1");
            fis1.setCredito(4);
            fis1.setQtdVagas(30);
            fis1.setHorario("Seg 08:00 - 10:00, Ter 16:00 - 18:00"); 
            fis1.setPreRequisito("NENHUM");
            fis1.setProfessor("Giuliano");
            fis1.setDescricao("Cinemática, leis de Newton e conservação de energia.");
            this.materiaRepository.save(fis1);

            // IPA
            var ipa = new MateriaModel();
            ipa.setCodigoMateria("COMP110");
            ipa.setNome("Introdução ao Pensamento Algorítmico");
            ipa.setCredito(2);
            ipa.setQtdVagas(60);
            ipa.setHorario("Sex 14:00 - 16:00");
            ipa.setPreRequisito("NENHUM");
            ipa.setProfessor("Marcela");
            ipa.setDescricao("Introduzir os algoritmos básicos em alto nível.");
            this.materiaRepository.save(ipa);
        }
        // Cria o aluno com email "bruno@teste.com"
        var alunoExistente = this.alunoRepository.findByEmail("bruno@teste.com");
        if (!alunoExistente.isPresent()) {
            MateriaModel[] materiaConcluir = {
                this.materiaRepository.findByCodigoMateria("MAT101").get()
            };
            criaAlunoExemplo(materiaConcluir);
        }
    }

    // Cria um aluno teste mockado
    private void criaAlunoExemplo(MateriaModel[] materiaConcluir){
        var alunoExemplo = new AlunoModel();
        alunoExemplo.setNome("Bruno Kenzo");
        alunoExemplo.setEmail("bruno@teste.com");

        var senhaCriptografado = new BCryptPasswordEncoder().encode("123");
        alunoExemplo.setSenha(senhaCriptografado);

        var alunoSalvo = this.alunoRepository.save(alunoExemplo);

        // Seta algumas matérias como "CONCLUIDA" no histórico
        for (MateriaModel materia : materiaConcluir){
            var historicoMatricula = new MatriculaModel();
            historicoMatricula.setAlunoID(alunoSalvo.getAlunoID());
            historicoMatricula.setMateriaID(materia.getMateriaID());
            historicoMatricula.setStatus("CONCLUIDA");

            this.matriculaRepository.save(historicoMatricula);
        }
    }
}
