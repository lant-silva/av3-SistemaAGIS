package br.fateczl.edu.SpringDataAGIS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "conteudo")
public class Conteudo {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@JoinColumn(name = "disciplina_codigo", nullable = false)
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	private Disciplina disciplina;
}
