package br.com.medstock.domain.repository;

import br.com.medstock.domain.model.Unidade;

import java.util.List;
import java.util.Optional;

public interface UnidadeDAO {

    Unidade save(Unidade unidade);

    Optional<Unidade> findById(Integer id);

    Optional<Unidade> findByNome(String nome);

    List<Unidade> findAll();

    void deleteById(Integer id);
}