package br.fateczl.edu.SpringDataAGIS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.fateczl.edu.SpringDataAGIS.model.Matricula;

@Repository
public interface IMatriculaRepository extends JpaRepository<Matricula, Integer>{
    @Query(value = "SELECT m.codigo AS matricula_codigo, md.disciplina_codigo, md.situacao, md.qtd_faltas, md.nota_final " +
                   "FROM matricula_disciplina md, matricula m, aluno a, curso c " +
                   "WHERE md.matricula_codigo = m.codigo " +
                   "AND m.codigo = :codigoMatricula " +
                   "AND a.curso_codigo = c.codigo " +
                   "AND m.aluno_ra = a.ra", nativeQuery = true)
    List<Object[]> findUltimaMatricula(@Param("codigoMatricula") Integer codigoMatricula);
    
    @Query(value = "SELECT TOP 1 codigo, aluno_ra, data_matricula FROM matricula WHERE aluno_ra = :ra ORDER BY codigo DESC", nativeQuery = true)
    Matricula consultarUltimaMatricula(@Param("ra") String ra);
    
    @Procedure(name = "Matricula.sp_gerarmatricula")
    int sp_gerarmatricula(@Param("ra") String ra);
    
    @Procedure(name = "Matricula.sp_inserirmatricula")
    String sp_inserirMatricula(@Param("ra") String ra, @Param("codigoMatricula") int codigoMatricula, @Param("codigoDisciplina") int codigoDisciplina);
}
