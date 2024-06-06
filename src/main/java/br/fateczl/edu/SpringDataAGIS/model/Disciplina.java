package br.fateczl.edu.SpringDataAGIS.model;

import java.time.LocalTime;

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
@Table(name = "disciplina")
public class Disciplina {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "qtd_aulas", nullable = false)
	private int qtdAulas;
	
	@Column(name = "horario_inicio", nullable = false)
	private LocalTime horarioInicio;
	
	@Column(name = "horario_fim", nullable = false)
	private LocalTime horarioFim;
	
	@Column(name = "dia", length = 10, nullable = false)
	private String dia;
	
	@JoinColumn(name = "curso_codigo", nullable = false)
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.EAGER)
	private Curso curso;
	
	@JoinColumn(name = "professor_codigo", nullable = false)
	@ManyToOne(targetEntity = Professor.class, fetch = FetchType.EAGER)
	private Professor professor;
}
