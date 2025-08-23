package br.com.medstock.domain.repository;

import br.com.medstock.domain.model.Funcionario;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository {

    Funcionario save(Funcionario funcionario);

    Optional<Funcionario> findById(Integer id);

    Optional<Funcionario> findByMatricula(String matricula);

    List<Funcionario> findAll();

    void deleteById(Integer id);
}