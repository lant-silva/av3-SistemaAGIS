package br.fateczl.edu.SpringDataAGIS.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matricula_disciplina")
@IdClass(MatriculaDisciplinaId.class)
@NamedNativeQuery(
		name = "MatriculaDisciplina.fn_alunochamada",
		query = "SELECT * FROM fn_alunochamada() WHERE disciplina_codigo = :codigo"
)
@NamedNativeQuery(
		name = "MatriculaDisciplina.fn_listarultimamatricula",
		query = "SELECT * FROM fn_listarultimamatricula(?1)",
		resultClass = MatriculaDisciplina.class
)
public class MatriculaDisciplina {
	
	@Id
	@JoinColumn(name = "matricula_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Matricula.class, fetch = FetchType.EAGER)
	private Matricula matricula;
	
	@Id
	@JoinColumn(name = "disciplina_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Disciplina.class, fetch = FetchType.EAGER)
	private Disciplina disciplina;
	
	@Column(name = "situacao", length = 50, nullable = false)
	private String situacao;
	
	@Column(name = "qtd_faltas", nullable = false)
	private int qtdFaltas;
	
	@Column(name = "nota_final", length = 3, nullable = false)
	private String notaFinal;
}
