package br.fateczl.edu.SpringDataAGIS.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "empregado")
@IdClass(DispensaId.class)
public class Dispensa {
	
	@Id
	@JoinColumn(name = "aluno_ra", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Aluno.class, fetch = FetchType.LAZY)
	private Aluno aluno;
	
	@Id
	@JoinColumn(name = "disciplina_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	private Disciplina disciplina;
	
	@Column(name = "motivo", length = 200, nullable = false)
	private String motivo;
	
	@Column(name = "estado", length = 200, nullable = false)
	private String estado;
}
