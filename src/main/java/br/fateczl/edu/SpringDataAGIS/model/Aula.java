package br.fateczl.edu.SpringDataAGIS.model;

import java.time.LocalDate;

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
@Table(name = "aula")
@IdClass(AulaId.class)
public class Aula {
	
	@Id
	@JoinColumn(name = "matricula_codigo", nullable = false)
	@JoinColumn(name = "disciplina_codigo", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = MatriculaDisciplina.class, fetch = FetchType.EAGER)
	private MatriculaDisciplina matriculaDisciplina;
	
	@Id
	@Column(name = "data_aula", nullable = false)
	private LocalDate dataAula;
	
	@Column(name = "presenca", nullable = false)
	private int presenca;
}
