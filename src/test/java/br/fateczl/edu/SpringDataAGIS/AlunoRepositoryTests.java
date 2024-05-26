package br.fateczl.edu.SpringDataAGIS;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;

@SpringBootTest
public class AlunoRepositoryTests {

    @Autowired
    private IAlunoRepository alunoRepository;

    @Test
    public void testInsertAluno() {
        Aluno aluno = new Aluno(
                "12345678901", "RA123456", "João Silva", "João",
                LocalDate.of(2000, 1, 1), "987654321", "123456789",
                "joao@example.com", "joao.corp@example.com",
                LocalDate.of(2018, 12, 1), "Escola X", 750.0, 10,
                "2019", "1", "2023/2", "2024/2",
                LocalDate.of(2019, 2, 1), "Manhã"
            );

            alunoRepository.save(aluno);

            Aluno found = alunoRepository.findById("RA123456").orElse(null);
            assertThat(found).isNotNull();
            assertThat(found.getNome()).isEqualTo("João Silva");
    }
    
    @Test
    public void testUpdateAluno() {
        Aluno aluno = new Aluno(
            "12345678901", "RA123456", "João Silva", "João",
            LocalDate.of(2000, 1, 1), "987654321", "123456789",
            "joao@example.com", "joao.corp@example.com",
            LocalDate.of(2018, 12, 1), "Escola X", 750.0, 10,
            "2019", "1", "2023/2", "2024/2",
            LocalDate.of(2019, 2, 1), "Manhã"
        );

        alunoRepository.save(aluno);

        aluno.setNome("João da Silva");
        alunoRepository.save(aluno);

        Aluno found = alunoRepository.findById("RA123456").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getNome()).isEqualTo("João da Silva");
    }
    
    @Test
    public void testDeleteAluno() {
        Aluno aluno = new Aluno(
            "12345678901", "RA123456", "João Silva", "João",
            LocalDate.of(2000, 1, 1), "987654321", "123456789",
            "joao@example.com", "joao.corp@example.com",
            LocalDate.of(2018, 12, 1), "Escola X", 750.0, 10,
            "2019", "1", "2023/2", "2024/2",
            LocalDate.of(2019, 2, 1), "Manhã"
        );

        alunoRepository.save(aluno);
        alunoRepository.deleteById("RA123456");

        Aluno found = alunoRepository.findById("RA123456").orElse(null);
        assertThat(found).isNull();
    }
    
    @Test
    public void testFindAlunoById() {
        Aluno aluno = new Aluno(
            "12345678901", "RA123456", "João Silva", "João",
            LocalDate.of(2000, 1, 1), "987654321", "123456789",
            "joao@example.com", "joao.corp@example.com",
            LocalDate.of(2018, 12, 1), "Escola X", 750.0, 10,
            "2019", "1", "2023/2", "2024/2",
            LocalDate.of(2019, 2, 1), "Manhã"
        );

        alunoRepository.save(aluno);

        Aluno found = alunoRepository.findById("RA123456").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getNome()).isEqualTo("João Silva");
    }
    
    @Test
    public void testFindAllAlunos() {
        Aluno aluno1 = new Aluno(
            "12345678901", "RA123456", "João Silva", "João",
            LocalDate.of(2000, 1, 1), "987654321", "123456789",
            "joao@example.com", "joao.corp@example.com",
            LocalDate.of(2018, 12, 1), "Escola X", 750.0, 10,
            "2019", "1", "2023/2", "2024/2",
            LocalDate.of(2019, 2, 1), "Manhã"
        );

        Aluno aluno2 = new Aluno(
            "98765432109", "RA654321", "Maria Souza", "Maria",
            LocalDate.of(1999, 5, 15), "912345678", "876543210",
            "maria@example.com", "maria.corp@example.com",
            LocalDate.of(2017, 11, 20), "Escola Y", 800.0, 5,
            "2018", "2", "2022/1", "2023/1",
            LocalDate.of(2018, 3, 1), "Noite"
        );

        alunoRepository.save(aluno1);
        alunoRepository.save(aluno2);

        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(2);
        assertThat(alunos.get(0).getNome()).isEqualTo("João Silva");
        assertThat(alunos.get(1).getNome()).isEqualTo("Maria Souza");
    }
    
    @Test
    public void testFindAlunoByInvalidId() {
        Aluno found = alunoRepository.findById("INVALID_ID").orElse(null);
        assertThat(found).isNull();
    }
}
