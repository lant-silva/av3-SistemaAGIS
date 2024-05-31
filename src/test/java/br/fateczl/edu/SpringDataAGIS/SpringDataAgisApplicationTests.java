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
		
		Matricula m1 = new Matricula(
				1001, aluno1, LocalDate.of(2022, 12, 1)
		);
		
		Matricula m2 = new Matricula(
				1002, aluno1, LocalDate.of(2022, 12, 12)
		);
		
		Matricula m3 = new Matricula(
				1003, aluno2, LocalDate.of(2022, 12, 1)
		);
		
		aRep.save(aluno1);
		aRep.save(aluno2);
		mRep.save(m1);
		mRep.save(m2);
		mRep.save(m3);
					
		Aluno aTemp = aRep.findById("RA123456").get();
		List<Matricula> temp = new ArrayList<>();
		List<Matricula> aux = new ArrayList<>();
		temp = mRep.findAll();
		for(Matricula m : temp) {
			if(m.getAluno().getRa() == "RA123456") {
				aux.add(m);
			} 
		}
		for(Matricula m : aux) {
			System.out.println(m.toString());
		}

		
		int ultimo = aux.size();
		Matricula mTemp = temp.get(ultimo);
		System.out.println(mTemp.toString());
		assertThat(mTemp != null);
		assertThat(mTemp.getCodigo() == 1002);
	}
	
	private void gerarValores() {
		
	}
}