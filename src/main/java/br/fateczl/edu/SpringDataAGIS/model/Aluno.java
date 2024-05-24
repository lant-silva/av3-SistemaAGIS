package br.fateczl.edu.SpringDataAGIS.model;

import java.sql.Date;
import java.time.LocalDate;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aluno")
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
	private String anoIntresso;
	
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
	
	@Id
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_codigo", nullable = false)
	private Curso curso;
}