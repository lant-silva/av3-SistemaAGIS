package br.fateczl.edu.SpringDataAGIS.model;

import java.sql.Date;

import jakarta.persistence.CascadeType;
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

	@Column(name = "cpf", nullable = false)
	private String cpf;
	
	@Id
	@Column(name = "ra", nullable = false)
	private String ra;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "nome_social", nullable = true)
	private String nomeSocial;
	
	@Column(name = "data_nasc", nullable = false)
	private Date dataNasc;
	
	@Column(name = "telefone_celular", nullable = false)
	private String telefoneCelular;
	
	@Column(name = "telefone_residencial", nullable = true)
	private String telefoneResidencial;
	
	@Column(name = "email_pessoal", nullable = false)
	private String emailPessoal;
	
	@Column(name = "email_corporativo", nullable = true)
	private String emailCorporativo;
	
	@Column(name = "data_segundograu", nullable = false)
	private Date dataSegundoGrau;
	
	@Column(name = "instituicao_segundograu", nullable = false)
	private String instituicaoSegundoGrau;
	
	@Column(name = "pontuacao_vestibular", nullable = false)
	private double pontuacaoVestibular;
	
	@Column(name = "posicao_vestibular", nullable = false)
	private int posicaoVestibular;
	
	@Column(name = "ano_ingresso", nullable = false)
	private String anoIntresso;
	
	@Column(name = "semestre_ingresso", nullable = false)
	private String semestreIngresso;
	
	@Column(name = "semestre_graduacao", nullable = false)
	private String semestreGraduacao;
	
	@Column(name = "ano_limite", nullable = false)
	private String anoLimite;
	
	@Column(name = "data_primeiramatricula", nullable = false)
	private String dataPrimeiraMatricula;
	
	@Column(name = "turno", nullable = false)
	private String turno;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_codigo", nullable = false)
	private Curso curso;
}
