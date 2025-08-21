package br.com.medstock.view;

import br.com.medstock.domain.model.*;

public class Main {
    public static void main(String[] args) {
        Material reagente = new Material("Reagente X", TipoMaterial.REAGENTE, 100);
        Unidade unidade1 = new Unidade("Laboratório Central", "Bloco A");
        ResponsavelAlmoxarifado joao = new ResponsavelAlmoxarifado("João", "12345");

        joao.registrarMaterial(reagente, 10);

        RegistroDeConsumo registro = new RegistroDeConsumo(reagente, unidade1, 10);
        System.out.println("Consumo registrado com sucesso.");
    }
}

