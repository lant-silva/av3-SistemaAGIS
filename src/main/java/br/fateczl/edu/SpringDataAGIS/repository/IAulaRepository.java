package br.fateczl.edu.SpringDataAGIS.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.fateczl.edu.SpringDataAGIS.model.Aula;

public interface IAulaRepository extends JpaRepository<Aula, Integer>{
	
	
	
	@Procedure(name = "Aula.sp_inseriraula")
	String sp_inseriraula(@Param("matriculaCodigo") int matricula, @Param("disciplinaCodigo") int disciplina, @Param("presenca") int presenca, @Param("dataAula") LocalDate dataAula);
}
