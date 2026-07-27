package com.projeto.matriculafacil.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.matriculafacil.matricula.MatriculaModel;
import com.projeto.matriculafacil.aluno.AlunoModel;
import com.projeto.matriculafacil.aluno.AlunoRepository;
import com.projeto.matriculafacil.materia.MateriaRepository;
import com.projeto.matriculafacil.materia.MateriaModel;
import com.projeto.matriculafacil.matricula.MatriculaRepository;

import lombok.RequiredArgsConstructor;

// Inicializa o banco de dados com algumas matérias
@Configuration
@RequiredArgsConstructor
public class DataInitializerSeed implements CommandLineRunner{
    
    private final MateriaRepository materiaRepository;
    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final PasswordEncoder passwordEncoder;

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
            calc1.setProfessor("Fábio");
            calc1.setDescricao("Limites, derivadas e integrais.");
            calc1.setDisponivel(true);
            this.materiaRepository.save(calc1);

            // Cálculo 2 
            var calc2 = new MateriaModel();
            calc2.setCodigoMateria("MAT102");
            calc2.setNome("Cálculo 2");
            calc2.setCredito(4);
            calc2.setQtdVagas(30);
            calc2.setHorario("Seg 10:00 - 12:00, Qua 08:00 - 10:00");
            calc2.setPreRequisito("MAT101"); 
            calc2.setProfessor("Bruna");
            calc2.setDescricao("Derivadas parciais e séries.");
            calc2.setDisponivel(true);
            this.materiaRepository.save(calc2);

            // Cálculo 3 
            var calc3 = new MateriaModel();
            calc3.setCodigoMateria("MAT103");
            calc3.setNome("Cálculo 3");
            calc3.setCredito(4);
            calc3.setQtdVagas(40);
            calc3.setHorario("Ter 14:00 - 18:00");
            calc3.setPreRequisito("MAT102"); 
            calc3.setProfessor("Marcos");
            calc3.setDescricao("Integrais com múltiplas variáveis.");
            calc3.setDisponivel(true);
            this.materiaRepository.save(calc3);

            // Algoritmo e estrutura de dados 1
            var aed1 = new MateriaModel();
            aed1.setCodigoMateria("COMP101");
            aed1.setNome("Algoritmos e Estrutura de Dados 1");
            aed1.setCredito(4);
            aed1.setQtdVagas(30);
            aed1.setHorario("Ter 08:00 - 12:00");
            aed1.setPreRequisito("NENHUM");
            aed1.setProfessor("Levada");
            aed1.setDescricao("Ordenação e árvores.");
            aed1.setDisponivel(true);
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
            fis1.setDisponivel(true);
            this.materiaRepository.save(fis1);
    
            // Introdução ao pensamento algorítmico
            var ipa = new MateriaModel();
            ipa.setCodigoMateria("COMP110");
            ipa.setNome("Introdução ao Pensamento Algorítmico");
            ipa.setCredito(2);
            ipa.setQtdVagas(30);
            ipa.setHorario("Sex 10:00 - 12:00");
            ipa.setPreRequisito("NENHUM");
            ipa.setProfessor("Marcela");
            ipa.setDescricao("Introduzir os algoritmos básicos em alto nível.");
            ipa.setDisponivel(true);
            this.materiaRepository.save(ipa);

            // Lógica digital
            var ld = new MateriaModel();
            ld.setCodigoMateria("COMP120");
            ld.setNome("Arquitetura e Organização de Computadores");
            ld.setCredito(6);
            ld.setQtdVagas(30);
            ld.setHorario("Seg 10:00 - 12:00, Sex 14:00 - 18:00");
            ld.setPreRequisito("NENHUM");
            ld.setProfessor("Menotti");
            ld.setDescricao("Álgebra booleana e verilog");
            ld.setDisponivel(false);
            this.materiaRepository.save(ld);

            // Geometria analítica
            var ga = new MateriaModel();
            ga.setCodigoMateria("MAT110");
            ga.setNome("Geometria Analítica");
            ga.setCredito(4);
            ga.setQtdVagas(40);
            ga.setHorario("Qua 08:00 - 12:00");
            ga.setPreRequisito("NENHUM");
            ga.setProfessor("Cláudia");
            ga.setDescricao("Vetores, matrizes, sistemas lineares e transformações lineares.");
            ga.setDisponivel(true);
            this.materiaRepository.save(ga);

            // Matemática discreta
            var md = new MateriaModel();
            md.setCodigoMateria("MAT120");
            md.setNome("Matemática Discreta");
            md.setCredito(4);
            md.setQtdVagas(35);
            md.setHorario("Seg 14:00 - 16:00, Qua 14:00 - 16:00");
            md.setPreRequisito("NENHUM");
            md.setProfessor("Homem");
            md.setDescricao("Lógica proposicional, teoria dos conjuntos, indução matemática e combinatória.");
            md.setDisponivel(true);
            this.materiaRepository.save(md);

            // Equações diferenciais ordinárias
            var edo = new MateriaModel();
            edo.setCodigoMateria("MAT130");
            edo.setNome("Equações Diferenciais Ordinárias");
            edo.setCredito(4);
            edo.setQtdVagas(30);
            edo.setHorario("Ter 19:00 - 21:00, Qui 19:00 - 21:00");
            edo.setPreRequisito("MAT102");
            edo.setProfessor("Roberto");
            edo.setDescricao("Equações de 1ª e 2ª ordem, transformada de Laplace e sistemas lineares.");
            edo.setDisponivel(true);
            this.materiaRepository.save(edo);

            // Física 2
            var fis2 = new MateriaModel();
            fis2.setCodigoMateria("FIS102");
            fis2.setNome("Física 2");
            fis2.setCredito(4);
            fis2.setQtdVagas(30);
            fis2.setHorario("Seg 19:00 - 21:00, Qua 19:00 - 21:00");
            fis2.setPreRequisito("FIS101");
            fis2.setProfessor("Maria");
            fis2.setDescricao("Oscilações, ondas mecânicas, fluidos e termodinâmica.");
            fis2.setDisponivel(true);
            this.materiaRepository.save(fis2);

            // Física 3 (Eletromagnetismo)
            var fis3 = new MateriaModel();
            fis3.setCodigoMateria("FIS103");
            fis3.setNome("Física 3");
            fis3.setCredito(4);
            fis3.setQtdVagas(25);
            fis3.setHorario("Ter 10:00 - 12:00, Qui 10:00 - 12:00");
            fis3.setPreRequisito("FIS102");
            fis3.setProfessor("Cláudia");
            fis3.setDescricao("Lei de Coulomb, campo elétrico, potencial, magnetismo e leis de Maxwell.");
            fis3.setDisponivel(true);
            this.materiaRepository.save(fis3);

            // AED 2
            var aed2 = new MateriaModel();
            aed2.setCodigoMateria("COMP102");
            aed2.setNome("Algoritmos e Estrutura de Dados 2");
            aed2.setCredito(6);
            aed2.setQtdVagas(30);
            aed2.setHorario("Ter 14:00 - 16:00, Qui 14:00 - 16:00, Sex 21:00 - 23:00");
            aed2.setPreRequisito("COMP101");
            aed2.setProfessor("Mario");
            aed2.setDescricao("Árvores balanceadas, tabelas hash, heaps e complexidade de algoritmos.");
            aed2.setDisponivel(true);
            this.materiaRepository.save(aed2);

            // Programação Orientada a Objetos
            var poo = new MateriaModel();
            poo.setCodigoMateria("COMP130");
            poo.setNome("Programação Orientada a Objetos");
            poo.setCredito(4);
            poo.setQtdVagas(35);
            poo.setHorario("Seg 08:00 - 12:00");
            poo.setPreRequisito("COMP101");
            poo.setProfessor("Bueno");
            poo.setDescricao("Classes, encapsulamento, herança, polimorfismo e padrões de projeto em Java.");
            poo.setDisponivel(false);
            this.materiaRepository.save(poo);

            // Teoria dos Grafos
            var grafos = new MateriaModel();
            grafos.setCodigoMateria("COMP104");
            grafos.setNome("Teoria dos Grafos");
            grafos.setCredito(4);
            grafos.setQtdVagas(30);
            grafos.setHorario("Qua 10:00 - 12:00, Sex 10:00 - 12:00");
            grafos.setPreRequisito("COMP102");
            grafos.setProfessor("Daniela");
            grafos.setDescricao("Busca em largura e profundidade, caminhos mínimos, árvores geradoras e fluxo.");
            grafos.setDisponivel(false);
            this.materiaRepository.save(grafos);

            // Sistemas Operacionais
            var so = new MateriaModel();
            so.setCodigoMateria("COMP105");
            so.setNome("Sistemas Operacionais");
            so.setCredito(6);
            so.setQtdVagas(25);
            so.setHorario("Seg 14:00 - 18:00, Qua 16:00 - 18:00");
            so.setPreRequisito("COMP120");
            so.setProfessor("Helio");
            so.setDescricao("Gerenciamento de processos, threads, concorrência, memória virtual e sistemas de arquivos.");
            so.setDisponivel(true);
            this.materiaRepository.save(so);

            // Inteligência Artificial
            var ia = new MateriaModel();
            ia.setCodigoMateria("COMP106");
            ia.setNome("Inteligência Artificial");
            ia.setCredito(4);
            ia.setQtdVagas(40);
            ia.setHorario("Sex 14:00 - 18:00");
            ia.setPreRequisito("MAT110");
            ia.setProfessor("Helena");
            ia.setDescricao("Agentes autônomos, busca heurística, raciocínio probabilístico e introdução ao aprendizado de máquina.");
            ia.setDisponivel(false);
            this.materiaRepository.save(ia);
        }
        // Cria o aluno com email "bruno@teste.com"
        var alunoExistente = alunoRepository.findByEmail("bruno@teste.com");
        if (!alunoExistente.isPresent()) {
            MateriaModel[] materiaConcluir = {
                materiaRepository.findByCodigoMateria("MAT101").orElseThrow(),
                materiaRepository.findByCodigoMateria("COMP110").orElseThrow(),
                materiaRepository.findByCodigoMateria("MAT102").orElseThrow(),
                materiaRepository.findByCodigoMateria("FIS101").orElseThrow(),
                materiaRepository.findByCodigoMateria("FIS102").orElseThrow(),
            };
            MateriaModel[] materiaReprovar = {
                materiaRepository.findByCodigoMateria("MAT120").orElseThrow(),
                materiaRepository.findByCodigoMateria("COMP101").orElseThrow(),
                materiaRepository.findByCodigoMateria("FIS103").orElseThrow()
            };
            criaAlunoExemplo(materiaConcluir, materiaReprovar);
        }
    }

    // Cria um aluno teste mockado
    private void criaAlunoExemplo(MateriaModel[] materiaConcluir, MateriaModel[] materiaReprovar){
        var alunoExemplo = new AlunoModel();
        alunoExemplo.setNome("Bruno Kenzo");
        alunoExemplo.setEmail("bruno@teste.com");
        alunoExemplo.setSenha(passwordEncoder.encode("1234567"));

        var alunoSalvo = alunoRepository.save(alunoExemplo);

        // Seta algumas matérias como "CONCLUIDA" no histórico
        for (MateriaModel materia : materiaConcluir){
            var historicoMatricula = new MatriculaModel();
            historicoMatricula.setAlunoID(alunoSalvo.getAlunoID());
            historicoMatricula.setMateriaID(materia.getMateriaID());
            historicoMatricula.setStatus("CONCLUIDA");

            matriculaRepository.save(historicoMatricula);
        }

        // Seta algumas matérias como "REPROVADA"
        for (MateriaModel materia : materiaReprovar){
            var historicoMatricula = new MatriculaModel();
            historicoMatricula.setAlunoID(alunoSalvo.getAlunoID());
            historicoMatricula.setMateriaID(materia.getMateriaID());
            historicoMatricula.setStatus("REPROVADA");

            matriculaRepository.save(historicoMatricula);
        }
    }
}
