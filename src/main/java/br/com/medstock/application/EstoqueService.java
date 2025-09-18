package br.com.medstock.application;

import br.com.medstock.domain.exception.RecursoNaoEncontradoException;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.repository.MaterialDAO;

public class EstoqueService {

    private static final int ASTRAZENECA_ID = 1;
    private final MaterialDAO materialDAO;

    public EstoqueService(MaterialDAO materialDAO) {
        this.materialDAO = materialDAO;
    }

    private Material getMaterial() {
        return materialDAO.findById(ASTRAZENECA_ID)
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
        return materialDAO.save(material);
    }

    public Material removerEstoque() {
        Material material = getMaterial();
        material.removerEstoque();
        return materialDAO.save(material);
    }
}