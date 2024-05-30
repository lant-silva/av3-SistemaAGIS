package br.fateczl.edu.SpringDataAGIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.fateczl.edu.SpringDataAGIS.model.Aluno;

@Repository
public interface IAlunoRepository extends JpaRepository<Aluno, String>{
	@Procedure(name = "Aluno.sp_gerarra")
	String sp_gerarra(@Param("ano") String ano, @Param("sem") String sem);
	
	@Procedure(name = "Aluno.sp_geraringresso")
	String sp_geraringresso(@Param("ano") String ano, @Param("sem") String sem);
	
	@Procedure(name = "Aluno.sp_geraranolimite")
	String sp_geraranolimite(@Param("ano") String ano, @Param("sem") String sem);
}