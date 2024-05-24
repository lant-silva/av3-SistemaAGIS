package br.fateczl.edu.SpringDataAGIS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "professor")
public class Professor {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "titulacao", length = 100, nullable = false)
	private String titulacao;
}
