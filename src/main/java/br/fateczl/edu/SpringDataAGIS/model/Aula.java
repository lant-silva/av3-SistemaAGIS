package br.fateczl.edu.SpringDataAGIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
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
	private MatriculaDisciplina matriculaDisciplina;
}
