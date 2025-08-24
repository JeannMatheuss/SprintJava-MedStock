package br.com.medstock.application;

import br.com.medstock.domain.exception.RecursoNaoEncontradoException;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.repository.MaterialRepository;

public class EstoqueService {

    private static final int ASTRAZENECA_ID = 1;
    private final MaterialRepository materialRepository;

    public EstoqueService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    private Material getMaterial() {
        return materialRepository.findById(ASTRAZENECA_ID)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Material 'AstraZeneca' com ID " + ASTRAZENECA_ID + " não foi encontrado no banco de dados."
                ));
    }

    public Material consultarEstoque() {
        return getMaterial();
    }

    public Material adicionarEstoque() {
        Material material = getMaterial();
        material.adicionarEstoque();
        return materialRepository.save(material);
    }

    public Material removerEstoque() {
        Material material = getMaterial();
        material.removerEstoque();
        return materialRepository.save(material);
    }
}