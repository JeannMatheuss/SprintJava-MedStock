package br.com.medstock.domain.repository;

import br.com.medstock.domain.model.RegistroDeConsumo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroDeConsumoRepository {

    RegistroDeConsumo save(RegistroDeConsumo registro);

    Optional<RegistroDeConsumo> findById(Integer id);

    List<RegistroDeConsumo> findAll();

    List<RegistroDeConsumo> findByPeriodo(LocalDate dataInicio, LocalDate dataFim);

    void deleteById(Integer id);
}