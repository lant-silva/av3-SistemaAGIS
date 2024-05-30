package br.fateczl.edu.SpringDataAGIS.repository;

import java.util.List;

import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;

public interface IMatriculaDisciplinaRepository {
	List<MatriculaDisciplina> fn_listarultimamatricula(String ra);
}
