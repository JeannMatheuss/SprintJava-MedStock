package br.com.medstock.domain.service;

import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.RegistroDeConsumo;
import br.com.medstock.domain.model.Unidade;

public class ConsumoService {
    public RegistroDeConsumo registrarConsumo(Material material, Unidade unidade, int quantidade) {
        if (quantidade > material.getQuantidadeDisponivel()) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque.");
        }
        material.consumir(quantidade);
        return new RegistroDeConsumo(material, unidade, quantidade);
    }
}
