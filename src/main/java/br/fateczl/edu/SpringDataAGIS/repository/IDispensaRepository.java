package br.fateczl.edu.SpringDataAGIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.fateczl.edu.SpringDataAGIS.model.Dispensa;

public interface IDispensaRepository extends JpaRepository<Dispensa, Integer>{
	@Procedure(name = "Dispensa.sp_alunodispensa")
	String sp_alunodispensa(@Param("ra") String ra, @Param("disciplina") int disciplina, @Param("motivo") String motivo);
}
