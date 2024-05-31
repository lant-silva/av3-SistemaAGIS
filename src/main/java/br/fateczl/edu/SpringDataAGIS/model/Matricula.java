package br.fateczl.edu.SpringDataAGIS.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matricula")
@NamedStoredProcedureQuery(
		name = "Matricula.sp_gerarmatricula",
		procedureName = "sp_gerarmatricula",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "codigo", type = Integer.class)
		}
		
)
@NamedStoredProcedureQuery(
		name = "Matricula.sp_inserirmatricula",
		procedureName = "sp_inserirmatricula",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoMatricula", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)
public class Matricula {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@JoinColumn(name = "aluno_ra", nullable = false)
	@ManyToOne(targetEntity = Aluno.class, fetch = FetchType.EAGER)
	private Aluno aluno;
	
	@Column(name = "data_matricula", nullable = false)
	private LocalDate dataMatricula;
}
