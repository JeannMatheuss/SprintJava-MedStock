package br.com.medstock.model;

import java.time.LocalDate;

public class RegistroDeConsumo {
    private Material material;
    private Unidade unidade;
    private int quantidade;
    private LocalDate data;

    public RegistroDeConsumo(Material material, Unidade unidade, int quantidade) {
        this.material = material;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.data = LocalDate.now();
        material.consumir(quantidade);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
