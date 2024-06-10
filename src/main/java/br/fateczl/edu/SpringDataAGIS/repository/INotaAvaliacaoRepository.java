package br.fateczl.edu.SpringDataAGIS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.fateczl.edu.SpringDataAGIS.model.NotaAvaliacao;

public interface INotaAvaliacaoRepository extends JpaRepository<NotaAvaliacao, Integer>{
	@Query(name = "NotaAvaliacao.fn_notasparciais", nativeQuery = true)
	List<NotaAvaliacao> findNotasParciais(String ra);
	
	@Query(name = "NotaAvaliacao.fn_listaralunos", nativeQuery = true)
	List<NotaAvaliacao> findAlunosPorAvaliacao(int codigoDisciplina, int codigoAvaliacao);
	
	@Procedure(name = "notaAvaliacao.sp_salvarnota")
	String sp_salvarnota(@Param("avaliacao") int avaliacao, @Param("disciplina") int disciplina, @Param("matricula") int matricula, @Param("nota") float nota);
}
