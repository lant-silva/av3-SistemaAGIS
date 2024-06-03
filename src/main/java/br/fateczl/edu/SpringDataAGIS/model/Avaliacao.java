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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avaliacao")
public class Avaliacao {
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;

	@JoinColumn(name = "disciplina_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Disciplina.class, fetch = FetchType.EAGER)
	private Disciplina disciplina;
	
	
	@Column(name = "nome", length = 20, nullable = false)
	private String nome;
	
	@Column(name = "peso", nullable = false)
	private float peso;
}
