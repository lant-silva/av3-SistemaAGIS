package br.fateczl.edu.SpringDataAGIS;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;

@SpringBootTest
class SpringDataAgisApplicationTests {
	
    @Autowired
    private IAlunoRepository alunoRepository;
	
	@Test
	void contextLoads() {

	}
}
