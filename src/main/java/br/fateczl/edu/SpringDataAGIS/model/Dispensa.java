package br.fateczl.edu.SpringDataAGIS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "dispensa")
@IdClass(DispensaId.class)
@NamedStoredProcedureQuery(
    name = "Dispensa.sp_alunodispensa",
    procedureName = "sp_alunodispensa",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "disciplina", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "motivo", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
    }
)
public class Dispensa {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aluno_ra", referencedColumnName = "ra", nullable = false)
    private Aluno aluno;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disciplina_codigo", referencedColumnName = "codigo", nullable = false)
    private Disciplina disciplina;

    @Column(name = "motivo", length = 200, nullable = false)
    private String motivo;

    @Column(name = "estado", length = 200, nullable = false)
    private String estado;
}
