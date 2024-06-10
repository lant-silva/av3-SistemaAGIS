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
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="nota_avaliacao")
@IdClass(NotaAvaliacaoId.class)
@NamedNativeQuery(
		name = "NotaAvaliacao.fn_notasparciais",
		query = "SELECT * FROM fn_notasparciais(?1)",
		resultClass = NotaAvaliacao.class
)
@NamedNativeQuery(
		name = "NotaAvaliacao.fn_listaralunos",
		query = "SELECT * FROM fn_listaralunos(?1,?2)",
		resultClass = NotaAvaliacao.class
)
@NamedStoredProcedureQuery(
		name = "NotaAvaliacao.sp_salvarnota",
		procedureName = "sp_salvarnota",
		parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "avaliacao", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "disciplina", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "matricula", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "nota", type = Float.class),
			@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)

public class NotaAvaliacao {
	@Id
	@JoinColumn(name = "disciplina_codigo", nullable = false)
	@JoinColumn(name = "matricula_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = MatriculaDisciplina.class, fetch = FetchType.EAGER)
	private MatriculaDisciplina matricula;
	
	@Id
	@JoinColumn(name = "avaliacao_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Avaliacao.class, fetch = FetchType.EAGER)
	private Avaliacao avaliacao;
	
	@Column(name = "nota", nullable = false)
	private float nota;
}
