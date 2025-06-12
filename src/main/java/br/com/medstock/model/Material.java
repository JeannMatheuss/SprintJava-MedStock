package br.com.medstock.model;

public class Material {
    private String nome;
    private String tipo;
    private int quantidadeDisponivel;

    public Material(String nome, String tipo, int quantidadeDisponivel) {
        this.nome = nome;
        this.tipo = tipo;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void consumir(int quantidade) {
        if (quantidade <= quantidadeDisponivel) {
            quantidadeDisponivel -= quantidade;
        } else{
            System.out.println("Estoque insufuciente.");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
}
