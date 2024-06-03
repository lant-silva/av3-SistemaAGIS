package br.fateczl.edu.SpringDataAGIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.fateczl.edu.SpringDataAGIS.model.Avaliacao;

@Repository
public interface IAvaliacaoRepository extends JpaRepository<Avaliacao, Integer>{

}
