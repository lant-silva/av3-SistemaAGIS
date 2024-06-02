package br.fateczl.edu.SpringDataAGIS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;

@Repository
public interface IMatriculaDisciplinaRepository extends JpaRepository<MatriculaDisciplina, Integer> {
	List<MatriculaDisciplina> fn_listarultimamatricula(String ra);
}
