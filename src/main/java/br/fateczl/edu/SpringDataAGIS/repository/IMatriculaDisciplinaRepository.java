package br.fateczl.edu.SpringDataAGIS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;

@Repository
public interface IMatriculaDisciplinaRepository extends JpaRepository<MatriculaDisciplina, Integer> {
	List<MatriculaDisciplina> fn_listarultimamatricula(String ra);
	
	@Query(value = "SELECT * FROM fn_situacaodisciplina()" +
			"WHERE disciplina_codigo = :disciplinaCodigo", nativeQuery = true)
	List<MatriculaDisciplina> listarAlunos(@Param("disciplinaCodigo") Integer disciplinaCodigo);
	
	@Query(nativeQuery = true, name = "MatriculaDisciplina.fn_alunochamada")
	List<MatriculaDisciplina> fn_alunochamada(@Param("codigo") int disciplinaCodigo);
}
