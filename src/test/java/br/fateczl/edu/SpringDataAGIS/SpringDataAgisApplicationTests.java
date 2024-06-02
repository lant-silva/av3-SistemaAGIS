package br.fateczl.edu.SpringDataAGIS;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.model.Matricula;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaRepository;

@SpringBootTest
class SpringDataAgisApplicationTests {
	
    @Autowired
    private IAlunoRepository aRep;
    
    @Autowired 
    private IMatriculaRepository mRep;
	
	@Test
	void contextLoads() {
		gerarValores();
	}
	
	private void gerarValores() {
		
	}
}