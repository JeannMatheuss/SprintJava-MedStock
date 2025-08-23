package br.com.medstock.domain.repository;

import br.com.medstock.domain.model.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository {

    Material save(Material material);

    Optional<Material> findById(int id);

    List<Material> findAll();

    List<Material> findByEstoqueAbaixoDe(int limite);

    void deleteById(int id);
}