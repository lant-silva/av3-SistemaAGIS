package br.fateczl.edu.SpringDataAGIS.model;

import java.io.Serializable;

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
public class AulaId implements Serializable{
	private static final long serialVersionVID = 1L;
	private Disciplina disciplina;
	private MatriculaDisciplina matriculaDisciplina;
}
