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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aluno")
@NamedStoredProcedureQuery(
		name = "Aluno.sp_geraranolimite",
		procedureName = "sp_geraranolimite",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ano", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "sem", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "anolimite", type = String.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Aluno.sp_gerarra",
		procedureName = "sp_gerarra",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ano", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "sem", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "ra", type = String.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Aluno.sp_geraringresso",
		procedureName = "sp_geraringresso",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "ano", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "sem", type = String.class)
		}
)
public class Aluno {

	@Column(name = "cpf", length = 11, nullable = false)
	private String cpf;
	
	@Id
	@Column(name = "ra", length = 9, nullable = false)
	private String ra;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "nome_social", length = 100, nullable = true)
	private String nomeSocial;
	
	@Column(name = "data_nasc", nullable = false)
	private LocalDate dataNasc;
	
	@Column(name = "telefone_celular", length = 9, nullable = false)
	private String telefoneCelular;
	
	@Column(name = "telefone_residencial", length = 9, nullable = true)
	private String telefoneResidencial;
	
	@Column(name = "email_pessoal", length = 200, nullable = false)
	private String emailPessoal;
	
	@Column(name = "email_corporativo", length = 200, nullable = true)
	private String emailCorporativo;
	
	@Column(name = "data_segundograu", nullable = false)
	private LocalDate dataSegundoGrau;
	
	@Column(name = "instituicao_segundograu", length = 100, nullable = false)
	private String instituicaoSegundoGrau;
	
	@Column(name = "pontuacao_vestibular", nullable = false)
	private double pontuacaoVestibular;
	
	@Column(name = "posicao_vestibular", nullable = false)
	private int posicaoVestibular;
	
	@Column(name = "ano_ingresso", length = 4, nullable = false)
	private String anoIngresso;
	
	@Column(name = "semestre_ingresso", length = 1, nullable = false)
	private String semestreIngresso;
	
	@Column(name = "semestre_graduacao", length = 6, nullable = false)
	private String semestreGraduacao;
	
	@Column(name = "ano_limite", length = 6, nullable = false)
	private String anoLimite;
	
	@Column(name = "data_primeiramatricula", nullable = false)
	private LocalDate dataPrimeiraMatricula;
	
	@Column(name = "turno", length = 10, nullable = false)
	private String turno;
	
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "curso_codigo", nullable = false)
	private Curso curso;
}
