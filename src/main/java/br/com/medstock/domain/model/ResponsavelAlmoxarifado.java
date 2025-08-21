package br.com.medstock.domain.model;

public class ResponsavelAlmoxarifado extends Funcionario {
    public ResponsavelAlmoxarifado(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public void registrarMaterial(Material m, int qtd) {
        m.consumir(qtd);
        System.out.println("Material atualizado no sistema.");
    }
}
