package br.com.medstock.service;

import br.com.medstock.model.Material;
import br.com.medstock.model.RegistroDeConsumo;
import br.com.medstock.model.Unidade;

public class ConsumoService {
    public RegistroDeConsumo registrarConsumo(Material material, Unidade unidade, int quantidade) {
        if (quantidade > material.getQuantidadeDisponivel()) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque.");
        }
        material.consumir(quantidade);
        return new RegistroDeConsumo(material, unidade, quantidade);
    }
}
