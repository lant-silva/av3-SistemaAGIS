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
		query = "SELECT * FROM fn_listaralunos(?1,?1)",
		resultClass = NotaAvaliacao.class
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
