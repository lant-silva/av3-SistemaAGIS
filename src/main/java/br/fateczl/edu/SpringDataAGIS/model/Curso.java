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
@Table(name = "curso")
public class Curso {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "carga_horaria", nullable = false)
	private int cargaHoraria;
	
	@Column(name = "sigla", length = 10, nullable = false)
	private String sigla;
	
	@Column(name = "nota_enade", nullable = false)
	private int notaEnade;
}
