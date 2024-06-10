package br.fateczl.edu.SpringDataAGIS.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.model.Aula;

public interface IAulaRepository extends JpaRepository<Aula, Integer>{
	@Query(value = "SELECT * FROM v_aluno_chamada WHERE codigo_disciplina = :codigoDisciplina", nativeQuery = true)
	List<Aluno> listarAlunos(@Param("codigoDisciplina") int disciplina);	
	
	@Procedure(name = "Aula.sp_verificaraula")
	boolean sp_verificaraula(@Param("disciplinaCodigo") int disciplina, @Param("dataAula") LocalDate dataAula);
	
	@Procedure(name = "Aula.sp_inseriraula")
	String sp_inseriraula(@Param("matriculaCodigo") int matricula, @Param("disciplinaCodigo") int disciplina, @Param("presenca") int presenca, @Param("dataAula") LocalDate dataAula);
}
